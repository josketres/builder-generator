package joske.test.data.builder.generator.model;

public class Property implements Comparable<Property> {

    private String type;
    private String name;
    private String setterName;
    private boolean shouldBeImported;
    private String qualifiedName;

    public int compareTo(Property o) {
        return getName().compareTo(o.getName());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public boolean isShouldBeImported() {
        return shouldBeImported;
    }

    public void setShouldBeImported(boolean shouldBeImported) {
        this.shouldBeImported = shouldBeImported;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

}
