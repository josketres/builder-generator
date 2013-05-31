import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class ReflectionBasedBuilderGenerator {

    private STGroup templates;

    public ReflectionBasedBuilderGenerator() {
        templates = new STGroupFile("builder.stg");
    }

    public String forClass(Class<?> clazz) {

        ST st = templates.getInstanceOf("builder");
        st.add("name", clazz.getSimpleName());
        st.add("qualifiedName", clazz.getName());
        addProperties(clazz, st);
        return st.render();
    }

    private void addProperties(Class<?> clazz, ST st) {
        PropertyDescriptor[] desc = PropertyUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : desc) {
            if (!descriptor.getPropertyType().equals(Class.class)) {
                Class<?> type = descriptor.getPropertyType();
                String name = descriptor.getName();
                st.addAggr("properties.{type,name,setterName}", type
                        .getSimpleName(), name, descriptor.getWriteMethod()
                        .getName());
            }
        }
    }
}
