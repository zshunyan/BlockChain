package es.unex.cume.incibe.sesion01;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class S502_CargarClaves_Firmar {

    public static void main(String[] args) throws Exception {
        // 1️ Cargar claves desde archivos
        PrivateKey privateKey = loadPrivateKey("privateKey.key");
        PublicKey publicKey = loadPublicKey("publicKey.key");

        // 2️ Mensaje a firmar
        String mensaje = "Mensaje importante";
        byte[] mensajeBytes = mensaje.getBytes();

        // 3️ Generar firma
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(mensajeBytes);
        byte[] firma = signature.sign();

        System.out.println(" Firma generada: " + Base64.getEncoder().encodeToString(firma));

        // 4️ Guardar la firma en un archivo
        saveSignatureToFile("firma.sig", firma);
        System.out.println(" Firma salvada en firma.sig");

        // 5 Verificar la firma
        signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(mensajeBytes);
        boolean esValida = signature.verify(firma);

        System.out.println(" ¿Firma válida? " + esValida);

    }

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

    public static void saveKey(String filename, Key key) throws IOException {
        String keyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(keyBase64);
        }
    }

    // Método para guardar la firma en Base64 en un archivo
    private static void saveSignatureToFile(String fileName, byte[] signature) throws IOException {
        String firmaBase64 = Base64.getEncoder().encodeToString(signature);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(firmaBase64);
        }
    }

    // Método para cargar la firma desde un archivo
    @SuppressWarnings("unused")
    private static byte[] loadSignatureFromFile(String fileName) throws IOException {
        String firmaBase64 = Files.readString(Path.of(fileName));
        return Base64.getDecoder().decode(firmaBase64);

    }
}
