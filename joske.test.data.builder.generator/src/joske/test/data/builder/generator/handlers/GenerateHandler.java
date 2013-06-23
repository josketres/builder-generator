package joske.test.data.builder.generator.handlers;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import joske.test.data.builder.generator.MetadataExtractor;
import joske.test.data.builder.generator.Renderer;
import joske.test.data.builder.generator.model.TargetClass;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class GenerateHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);
        ISelection sel = HandlerUtil.getActiveMenuSelection(event);
        IStructuredSelection selection = (IStructuredSelection) sel;

        Object firstElement = selection.getFirstElement();
        if (firstElement instanceof ICompilationUnit) {
            createOutput((ICompilationUnit) firstElement);
        } else {
            MessageDialog.openInformation(shell, "Info",
                    "Please select a Java source file");
        }
        return null;
    }

    private void createOutput(ICompilationUnit cu) {

        IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
                .getFolder(cu.getParent().getPath());
        write(folder, cu);
    }

    private void write(IFolder folder, ICompilationUnit cu) {
        
        Class<?> targetClass = null;
        try {
            String fullyQualifiedName = cu.getTypes()[0].getFullyQualifiedName();
            System.out.println(fullyQualifiedName);
            
            String projectName = cu.getJavaProject().getElementName();
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(cu.getJavaProject());

            List<URL> urlList = new ArrayList<URL>();
            for (int i = 0; i < classPathEntries.length; i++) {
             String entry = classPathEntries[i];
             IPath path = new Path(entry);
             URL url = path.toFile().toURI().toURL();
             urlList.add(url);
            }

            ClassLoader parentClassLoader = project.getClass().getClassLoader();
            URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
            URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
            targetClass = classLoader.loadClass(fullyQualifiedName);
        } catch (JavaModelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        TargetClass metadata = new MetadataExtractor(targetClass).getMetadata();
        String source = new Renderer().render(metadata);
        
        BufferedWriter writer = null;
        try {
            cu.getCorrespondingResource().getName();
            String test = cu.getCorrespondingResource().getName();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                    out));
            writer = new BufferedWriter(output);
            writer.write(source);
            writer.flush();

            // Need
            String[] name = test.split("\\.");
            IFile file = folder.getFile(name[0] + "Builder.java");
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            file.create(in, false, null);
        } catch (JavaModelException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }

    }

    private void close(BufferedWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
