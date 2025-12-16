package es.unex.cume.incibe.sesion01;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class S0302_SalvarRSA {
    public static void main(String[] args) throws Exception {
        // 1️ Generar un par de claves RSA
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 2️ Guardar las claves en archivos
        saveKey("publicKey.key", keyPair.getPublic().getEncoded());
        saveKey("privateKey.key", keyPair.getPrivate().getEncoded());

        System.out.println("Claves guardadas correctamente.");
    }

    // Método para guardar una clave en un archivo en formato Base64
    private static void saveKey(String fileName, byte[] keyBytes) throws IOException {
        String keyBase64 = Base64.getEncoder().encodeToString(keyBytes);
        try (FileWriter fw = new FileWriter(fileName);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(keyBase64);
        }
    }
}
