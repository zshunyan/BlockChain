package es.unex.cume.incibe.sesion01;

import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class S301CifradoRSA {

    public static void main(String[] args) throws Exception {
        // 1️ Generar el par de claves RSA (pública y privada)
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048); // Longitud de clave segura (mínimo 2048 bits)
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Publica\n"+Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("Privada\n"+Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        // 2️ Mensaje a cifrar
        String mensaje = "Hola, RSA en Java!";
        System.out.println("Mensaje original: " + mensaje);

        // 3️ Cifrar el mensaje con la clave pública
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes());
        String mensajeCifradoBase64 = Base64.getEncoder().encodeToString(mensajeCifrado);
        System.out.println("Mensaje cifrado: " + mensajeCifradoBase64);

        // 4️ Descifrar el mensaje con la clave privada
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] mensajeDescifrado = cipher.doFinal(Base64.getDecoder().decode(mensajeCifradoBase64));
        System.out.println("Mensaje descifrado: " + new String(mensajeDescifrado));
    }
}
