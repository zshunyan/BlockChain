package es.unex.cume.incibe.sesion01;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class S0303_CargarClaveRSA {
    public static void main(String[] args) throws Exception {
        // 1️ Cargar claves desde archivos
        PublicKey publicKey = loadPublicKey("publicKey.key");
        PrivateKey privateKey = loadPrivateKey("privateKey.key");

        System.out.println("Claves cargadas correctamente.");
        System.out.println("Clave pública: " + publicKey);
        System.out.println("Clave privada: " + privateKey);
    }

    // Método para cargar la clave pública desde archivo
    private static PublicKey loadPublicKey(String filePath) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readString(Path.of(filePath)));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // Método para cargar la clave privada desde archivo
    private static PrivateKey loadPrivateKey(String filePath) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(Files.readString(Path.of(filePath)));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
}
