package healthcare.housing.models;

import healthcare.housing.controllers.Security;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=2, max=25)
    private String firstName;

    @NotNull
    @Size(min=2, max=25)
    private String lastName;

    @Email
    private String email;

    @Size(min=6, max=20)
    @NotNull
    private String username;

    @Size(max=256)
    private String passHash;

    @Size(max = 5)
    private String passSalt;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String password) {
        this.passHash = Security.hashPass(password);
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt() {
        this.passSalt = Security.saltPass();
    }
}
