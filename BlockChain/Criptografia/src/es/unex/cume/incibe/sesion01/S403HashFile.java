package es.unex.cume.incibe.sesion01;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class S403HashFile {
    public static void main(String[] args) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA3-384");
            byte[] buffer = new byte[1000];
            FileInputStream in= new FileInputStream("S101HashList.java");
            int leidos = in.read(buffer, 0, 1000);
            while (leidos != -1) {
                messageDigest.update(buffer); // Texto claro a resumen
                leidos = in.read(buffer, 0, 1000);
            }
            in.close();
            byte[] resumen = messageDigest.digest(); // Completar el resumen
            // Mostrar resumen
            System.out.println("RESUMEN:");
            System.out.write(resumen, 0, resumen.length);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }
}
