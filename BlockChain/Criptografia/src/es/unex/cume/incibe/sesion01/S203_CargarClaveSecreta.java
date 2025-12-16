package es.unex.cume.incibe.sesion01;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.util.Base64;

public class S203_CargarClaveSecreta {
	public static void main(String[] args) throws Exception {

		String fileName = "ClaveSimetrica.key";

		String keyBase64 = Files.readString(Path.of(fileName));
		byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
		SecretKey secretKey = new SecretKeySpec(decodedKey, "AES");
		System.out.println(
				" Clave AES cargada correctamente: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
	}

}
