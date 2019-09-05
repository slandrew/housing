package healthcare.housing;


import healthcare.housing.controllers.Security;

public class SecurityTest {
    public static void main(String[] args){
        String hashedPass = Security.hashPass("b");
        String passSalt = Security.saltPass();
        System.out.println(hashedPass);
        System.out.println(passSalt);
    }
}
