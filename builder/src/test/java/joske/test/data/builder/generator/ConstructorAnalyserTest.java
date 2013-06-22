package joske.test.data.builder.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import joske.test.data.builder.generator.model.ConstructorSignature;

import org.junit.Test;

public class ConstructorAnalyserTest {

    static class Data {

    }

    static class TestClass {
        public TestClass(String argument1,
                String argument2,
                Data data,
                long longArg,
                int intArg,
                List<Object> generic,
                int... varargs) {

        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetConstructorParameterNames() throws Exception {

        ConstructorSignature signature = new ConstructorAnalyser()
                .getSignature(TestClass.class
                        .getConstructors()[0]);
        assertThat(
                signature.getNames(),
                contains("argument1",
                        "argument2",
                        "data",
                        "longArg",
                        "intArg",
                        "generic",
                        "varargs"));
        assertThat(
                signature.getTypes(),
                contains("Ljava/lang/String;",
                        "Ljava/lang/String;",
                        "Lcom/joske/builder/ConstructorAnalyserTest$Data;",
                        "J",
                        "I",
                        "Ljava/util/List;",
                        "[I"));
        assertThat(
                signature.getGenerics(),
                contains(null,
                        null,
                        null,
                        null,
                        null,
                        "Ljava/util/List<Ljava/lang/Object;>;",
                        null));
        assertThat(
                signature.getClassTypes(),
                contains(String.class,
                        String.class,
                        Data.class,
                        long.class,
                        int.class,
                        List.class,
                        int[].class));
    }
}
