package es.unex.cume.incibe.sesion01;

import java.security.*;

public class S501_FirmaBasica {

    public static void main(String[] args) throws Exception {
        // 1️ Generar un par de claves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 2048 bits para seguridad fuerte
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 2️ Mensaje a firmar
        String mensaje = "Este es un mensaje seguro";
        byte[] mensajeBytes = mensaje.getBytes();

        // 3️ Generar la firma digital
        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initSign(privateKey);
        signature.update(mensajeBytes);
        byte[] firma = signature.sign();

        System.out.println("Firma digital generada: " + bytesToHex(firma));

        // 4️ Verificar la firma con la clave pública
        signature.initVerify(publicKey);
        signature.update(mensajeBytes);
        boolean esValida = signature.verify(firma);

        System.out.println("¿Firma válida? " + esValida);
    }

    // Método para convertir bytes en representación hexadecimal
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
