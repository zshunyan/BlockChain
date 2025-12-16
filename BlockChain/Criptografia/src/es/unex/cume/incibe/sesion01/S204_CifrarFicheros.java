package es.unex.cume.incibe.sesion01;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;

public class S204_CifrarFicheros {
	public static void main(String[] args) throws Exception {

		String file="el_quijote.txt";
		
		/* PASO 1: Cargar la clave simétrica */
		
		String fileName = "ClaveSimetrica.key";

		String keyBase64 = Files.readString(Path.of(fileName));
		byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
		SecretKey clave = new SecretKeySpec(decodedKey, "AES");

		System.out.println("CLAVE:");
		mostrarBytes(clave.getEncoded());
		System.out.println();

		/* PASO 2: Crear cifrador */
		Cipher cifrador = Cipher.getInstance("AES");
		/*****************************************************************/
		System.out.println("2. Cifrar con AES el fichero "+file+
				           ", dejar el resultado en "+file+".cifrado");

		/* PASO 3a: Inicializar cifrador en modo CIFRADO */
		cifrador.init(Cipher.ENCRYPT_MODE, clave);
		
		/* Leer fichero de 1k en 1k y pasar fragmentos leidos al cifrador */
		byte[] buffer = new byte[1000];
		byte[] bufferCifrado;

		FileInputStream in = new FileInputStream(file);
		FileOutputStream out = new FileOutputStream(file+".cifrado");
		
		int bytesLeidos = in.read(buffer, 0, 1000);
		while (bytesLeidos != -1) { // Mientras no se llegue al final del fichero
			bufferCifrado = cifrador.update(buffer, 0, bytesLeidos); // Pasa texto claro leido al cifrador
			out.write(bufferCifrado); // Escribir texto cifrado
			bytesLeidos = in.read(buffer, 0, 1000);
		}
		bufferCifrado = cifrador.doFinal(); // Completar cifrado (procesa relleno, puede devolver texto)
		out.write(bufferCifrado); // Escribir final del texto cifrado (si lo hay) 
		
		in.close();
		out.close();
		
		/*****************************************************************/
		System.out.println("3. Descifrar con AES el fichero "+file+".cifrado"+
		           ", dejar el resultado en "+file+".descifrado");
		
		/* PASO 3b: Poner cifrador en modo DESCIFRADO */
		cifrador.init(Cipher.DECRYPT_MODE, clave);

		in = new FileInputStream(file+".cifrado");
		out = new FileOutputStream(file+".descifrado");
		byte[] bufferPlano;
		
		bytesLeidos = in.read(buffer, 0, 1000);
		while (bytesLeidos != -1) { // Mientras no se llegue al final del fichero
			bufferPlano = cifrador.update(buffer, 0, bytesLeidos); // Pasa texto claro leido al cifrador
			out.write(bufferPlano); // Escribir texto descifrado
			bytesLeidos = in.read(buffer, 0, 1000);
		}
		bufferPlano = cifrador.doFinal(); // Completar descifrado (procesa relleno, puede devolver texto)
		out.write(bufferPlano); // Escribir final del texto descifrado (si lo hay)
		
		in.close();
		out.close();
	} // Fin main()

	public static void mostrarBytes(byte [] buffer) {
		System.out.write(buffer, 0, buffer.length);
	} 
	
}
