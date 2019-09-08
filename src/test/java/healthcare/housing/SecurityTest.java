package healthcare.housing;


import healthcare.housing.controllers.Security;
import healthcare.housing.models.Session;

public class SecurityTest {
    public static void main(String[] args){
        Session testSession = new Session();
        String a = testSession.getSessionId();
        long b = testSession.getSessionStart();
        long c = testSession.getSessionEnd();
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
}
