package es.unex.cume.incibe.sesion01;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import java.nio.file.*;

public class S304_CifrarFicherosRSA {
	public static void main(String[] args) throws Exception {

		String file="el_quijote.txt";
		
		/* PASO 1: Cargar las claves asimétrica */
		
		PublicKey publicKey = loadPublicKey("publicKey.key");
        PrivateKey privateKey = loadPrivateKey("privateKey.key");

		/* PASO 2: Crear cifrador */
		Cipher cifrador = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		/*****************************************************************/
		System.out.println("2. Cifrar con RSA el fichero "+file+
				           ", dejar el resultado en "+file+".cifrado");

		/* PASO 3a: Inicializar cifrador en modo CIFRADO */
		cifrador.init(Cipher.ENCRYPT_MODE, privateKey);
		
		/* IMPORTANTE: En RSA solo se pueden cifrar en bloque de 245 bytes con clave 2048*/
		byte[] buffer = new byte[245];
		byte[] bufferCifrado;

		FileInputStream in = new FileInputStream(file);
		FileOutputStream out = new FileOutputStream(file+".cifradoRSA");
		
		int bytesLeidos = in.read(buffer, 0, 245);
		while (bytesLeidos != -1) { // Mientras no se llegue al final del fichero
			bufferCifrado = cifrador.doFinal(buffer, 0, bytesLeidos); // Pasa texto claro leido al cifrador
			out.write(bufferCifrado); // Escribir texto cifrado
			bytesLeidos = in.read(buffer, 0, 245);
		}
		
		in.close();
		out.close();
		
		/*****************************************************************/
		System.out.println("3. Descifrar con RSA el fichero "+file+".cifrado"+
		           ", dejar el resultado en "+file+".descifrado");
		
		/* PASO 3b: Poner cifrador en modo DESCIFRADO */
		cifrador.init(Cipher.DECRYPT_MODE, publicKey);

		in = new FileInputStream(file+".cifradoRSA");
		out = new FileOutputStream(file+".descifradoRSA");
		byte[] bufferPlano;
		
		/* Importante. Para descifrar, aunque se ha hecho con 245 bytes, se debe hacer con 256 porque añade información.  */
		buffer = new byte[256];
		bytesLeidos = in.read(buffer, 0, 256);
		while (bytesLeidos != -1) { // Mientras no se llegue al final del fichero
			bufferPlano = cifrador.doFinal(buffer, 0, bytesLeidos); // Pasa texto claro leido al cifrador
			out.write(bufferPlano); // Escribir texto descifrado
			bytesLeidos = in.read(buffer, 0, 256);
		}
		
		in.close();
		out.close();
	} // Fin main()

	public static void mostrarBytes(byte [] buffer) {
		System.out.write(buffer, 0, buffer.length);
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
