package healthcare.housing.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Posting {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String address;

    private String city;

    private String state;

    @ManyToOne
    private User user;

    private String description;

    @ElementCollection
    private List<String> pictureURLs = new ArrayList<>();

    private String title;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPictureURLs() {
        return pictureURLs;
    }

    public void setPictureURLs(List<String> pictureURLs) {
        this.pictureURLs = pictureURLs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void addPictureURL(String URL) {
        this.pictureURLs.add(URL);
    }

    public void removePictureURL(String URL) {
        this.pictureURLs.remove(URL);
    }
}
