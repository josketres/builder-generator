package joske.test.data.builder.generator;

import joske.test.data.builder.generator.model.TargetClass;

public class Generator {

    public String generate(Class<?> targetClass) {

        TargetClass metadata = new MetadataExtractor(targetClass).getMetadata();
        return new Renderer().render(metadata);
    }
}
