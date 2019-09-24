package healthcare.housing.models;

public enum State {

    Alabama("AL"),
    Alaska ("AK"),
    Arizona ("AZ"),
    Arkansas ("AR"),
    California ("CA"),
    Colorado ("CO"),
    Connecticut ("CT"),
    Delaware ("DE"),
    Florida ("FL"),
    Georgia ("GA"),
    Hawaii ("HI"),
    Idaho ("ID"),
    Illinois ("IL"),
    Indiana ("IN"),
    Iowa ("IA"),
    Kansas ("KS"),
    Kentucky ("KY"),
    Louisiana ("LA"),
    Maine ("ME"),
    Maryland ("MD"),
    Massachusetts("MA"),
    Michigan ("MI"),
    Minnesota ("MN"),
    Mississippt ("MS"),
    Missouri ("MO"),
    Montana ("MT"),
    Nebraska ("NE"),
    Nevada ("NV"),
    NewHampshire ("NH"),
    NewJersey ("NJ"),
    NewMexico ("NM"),
    NewYork ("NY"),
    NorthCarolina ("NC"),
    NorthDakota ("ND"),
    Ohio ("OH"),
    Oklahoma ("OK"),
    Oregon ("OR"),
    Pennsylvania ("PA"),
    RhodeIsland ("RI"),
    SouthCarolina ("SC"),
    SouthDakota ("SD"),
    Tennessee ("TN"),
    Texas ("TX"),
    Utah ("UT"),
    Vermont ("VT"),
    Virgina ("VA"),
    Washington ("WA"),
    WestVirgina ("WV"),
    Wisconsin ("WI"),
    Wyoming ("WY");

    private final String name;

    State(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
