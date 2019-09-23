package healthcare.housing.models;

public enum State {

    Missouri ("MO"),
    Illinois ("IL"),
    Kansas ("KS");

    private final String name;

    State(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
