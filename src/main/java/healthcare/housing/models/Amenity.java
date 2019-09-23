package healthcare.housing.models;

public enum Amenity {

    pool ("Pool"),
    privateBathroom ("Private Bathroom"),
    laundry ("Laundry");

    private final String name;

    Amenity(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
