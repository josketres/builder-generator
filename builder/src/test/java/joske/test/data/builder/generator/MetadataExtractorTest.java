package joske.test.data.builder.generator;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import joske.test.data.builder.generator.model.Property;
import joske.test.data.builder.generator.model.TargetClass;

import org.junit.Test;

import test.classes.Address;
import test.classes.NormalJavaBean;


public class MetadataExtractorTest {

    @Test
    public void shouldGenerateMetadataOfTargetClass() {
        MetadataExtractor gen = new MetadataExtractor(NormalJavaBean.class);
        TargetClass data = gen.getMetadata();
        assertEquals(NormalJavaBean.class.getSimpleName(), data.getName());
        assertEquals(NormalJavaBean.class.getName(), data.getQualifiedName());
    }

    @Test
    public void shouldGeneratePropertiesMetadata() throws Exception {
        MetadataExtractor gen = new MetadataExtractor(NormalJavaBean.class);
        TargetClass data = gen.getMetadata();
        assertEquals(NormalJavaBean.class.getSimpleName(), data.getName());
        assertEquals(NormalJavaBean.class.getName(), data.getQualifiedName());
        assertEquals(NormalJavaBean.class.getPackage().getName(), data.getPackageName());

        List<Property> properties = data.getProperties();
        propertyIs(properties.get(0), Address.class, "address", "setAddress", false);
        propertyIs(properties.get(1), int.class, "age", "setAge", false);
        propertyIs(properties.get(2), Date.class, "date", "setDate", true);
        propertyIs(properties.get(3), String.class, "name", "setName", false);

        assertEquals(4, properties.size());
    }

    private void propertyIs(Property property,
            Class<?> clazz, String name, String setterName,
            boolean shouldBeImported) {

        assertEquals(name, property.getName());
        assertEquals(clazz.getSimpleName(), property.getType());
        assertEquals(clazz.getName(), property.getQualifiedName());
        assertEquals(setterName, property.getSetterName());
        assertEquals(shouldBeImported, property.isShouldBeImported());
    }
}
