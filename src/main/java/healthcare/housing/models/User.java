package healthcare.housing.models;

import healthcare.housing.controllers.Security;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

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

    @Size(max=256)
    private String passHash;

    @Size(max = 5)
    private String passSalt;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Session> sessionList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Posting> postingList = new ArrayList<>();

    private int emailVerified;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User() {
        this.role = Role.GUEST;
    }

    public int getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(int emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void addSession (Session session) {
        this.sessionList.add(session);
    }

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

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String password) {
        this.passHash = Security.hashPass(password);
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public List<Posting> getPostingList() {
        return postingList;
    }

    public void setPostingList(List<Posting> postingList) {
        this.postingList = postingList;
    }
}
