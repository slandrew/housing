package healthcare.housing.models;

public enum Role {
    SUPERADMIN (0, "Super Admin"),
    ADMIN (1, "Admin"),
    USER (2, "User"),
    GUEST (3, "Guest");

    private final int intValue;

    private final String name;

    Role (int intValue, String name){
        this.intValue = intValue;
        this.name = name;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getName() {
        return name;
    }
}
