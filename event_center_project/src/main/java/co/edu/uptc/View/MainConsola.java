package co.edu.uptc.View;

import co.edu.uptc.Security.PasswordSecurityService;

public class MainConsola {
    public static void main(String[] args) {
        PasswordSecurityService ps = new PasswordSecurityService();
        String hash = ps.encrypt("Admin.2025");
        System.out.println(hash);
        System.out.println("Largo del hash: " + hash.length());
    }
}
