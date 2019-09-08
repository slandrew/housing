package healthcare.housing.controllers;

import java.util.Random;

public class Security {


    private static String hasher(String f){
        String a = "kKm8MV6v1jJZzIiNnY4yTt0GgLlDdBbsSHh7cCXxf3FEeUu9Oo5WwaArRQ2qpP";
        String b = "";
        int c = 0;
        int d = 0;
        int e = 0;
        for (char g: f.toCharArray()){
            d = d + a.indexOf(g);
        }
        while (b.length() < 256){
            e = b.length() + f.length() + d + b.length();
            char h = a.charAt((c + a.indexOf(f.charAt(c)) + d - c + e) % a.length());
            b = b + h;
            c = (c + 1) % f.length();
        }
        return b;
    }
    public static String hashPass(String password){
        String hashedPass = hasher(password);
        return hashedPass;
    }
    public static String saltPass(){
        String a = "kKm8MV6v1jJZzIiNnY4yTt0GgLlDdBbsSHh7cCXxf3FEeUu9Oo5WwaArRQ2qpP";
        String b = "";
        Random rand = new Random();
        for (int i = 0; i < 5; i++){
            b = b + a.charAt(rand.nextInt(a.length()));
        }
        return b;
    }

    public static String generateSessionId() {
        String a = "kKm8MV6v1jJZzIiNnY4yTt0GgLlDdBbsSHh7cCXxf3FEeUu9Oo5WwaArRQ2qpP";
        String b ="";
        Random rand = new Random();
        while (b.length() < 256) {
            b = b + a.charAt(rand.nextInt(a.length()));
        }
        return b;
    }
}
