package es.unex.cume.incibe.sesion01;

import java.security.MessageDigest;
import java.util.Base64;

public class S402Hash {
    public static void main(String[] args) throws Exception {
        String text = "Hola, Mundo!";
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = md.digest(text.getBytes());
        System.out.println(Base64.getEncoder().encodeToString(hash));
    }
}
