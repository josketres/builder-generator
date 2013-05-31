import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.gradle.Person;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionBasedBuilderGeneratorTest {

    private static final int COMPILER_SUCCESS_CODE = 0;

    @Test
    public void test() throws IOException {
        ReflectionBasedBuilderGenerator generator = new ReflectionBasedBuilderGenerator();
        String source = generator.forClass(Person.class);
        System.out.println(source);

        File root = new File("/java");
        File sourceFile = new File(root, "org/gradle/PersonBuilder.java");
        sourceFile.getParentFile().mkdirs();
        new FileWriter(sourceFile).append(source).close();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, sourceFile.getPath());
        Assert.assertEquals(COMPILER_SUCCESS_CODE, result);
    }
}
