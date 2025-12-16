package es.unex.cume.incibe.sesion01;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class S201CifradoAES {
    public static void main(String[] args) {
        try {
            /* PASO 1: Crear e inicializar clave */
            System.out.println("1. Generar clave AES");
            KeyGenerator generadorAES = KeyGenerator.getInstance("AES");
            generadorAES.init(256); // clave de 256 bits
            SecretKey clave = generadorAES.generateKey();

            /* PASO 2: Crear cifrador */
            Cipher cifrador = Cipher.getInstance("AES");

            /* PASO 3a: Inicializar cifrador en modo CIFRADO */
            cifrador.init(Cipher.ENCRYPT_MODE, clave);
            String texto = "Esto es una prueba de cifrado";
            byte[] mensajeCifrado = cifrador.doFinal(texto.getBytes());
            String mensajeCifradoBase64 = Base64.getEncoder().encodeToString(mensajeCifrado);
            System.out.println("Mensaje cifrado: " + mensajeCifradoBase64);
            /* PASO 3b: Poner cifrador en modo AESCIFRADO */
            // 4. Descifrar el mensaje
            cifrador.init(Cipher.DECRYPT_MODE, clave);
            byte[] mensajeDescifrado = cifrador.doFinal(Base64.getDecoder().decode(mensajeCifradoBase64));
            System.out.println("Mensaje descifrado: " + new String(mensajeDescifrado));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
