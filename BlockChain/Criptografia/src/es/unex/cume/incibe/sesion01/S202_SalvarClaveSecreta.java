package es.unex.cume.incibe.sesion01;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class S202_SalvarClaveSecreta {
	public static void main(String[] args) throws Exception {

		String fileName = "ClaveSimetrica.key";
		System.out.println("Crea el fichero claveSimetrica.secreta");

		/*** Paso 1. Crear e inicializar clave DES */
		KeyGenerator generadorDES = KeyGenerator.getInstance("AES");
		generadorDES.init(256);
		SecretKey claveSecreta = generadorDES.generateKey();

		/***
		 * Paso 2. Obtener en formato Base64 y guardarlo
		 */
		String keyBase64 = Base64.getEncoder().encodeToString(claveSecreta.getEncoded());
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write(keyBase64);
		}
		System.out.println("Clave AES guardada en ClaveSimetrica.key");
	}

}
