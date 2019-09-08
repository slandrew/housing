package healthcare.housing.models;

import healthcare.housing.controllers.Security;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Session {

    @Id
    @GeneratedValue
    private int id;

    private long sessionStart;

    private long sessionEnd;

    @Size(min = 256, max = 256)
    private String sessionId;

    @ManyToOne
    private User user;

    public long getSessionStart() {
        return sessionStart;
    }


    public Session () {
        this.setSessionStart();
        this.setSessionEnd();
        this.setSessionId();
    }

    public int getId() {
        return id;
    }


    public void setSessionStart() {
        long currentTime = System.currentTimeMillis();
        this.sessionStart = currentTime;
    }

    public long getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd() {
        long currentTime = System.currentTimeMillis();
        this.sessionEnd = (currentTime + 900000);
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId() {
        this.sessionId = Security.generateSessionId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
