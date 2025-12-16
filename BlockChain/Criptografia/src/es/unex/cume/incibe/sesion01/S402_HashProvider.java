package es.unex.cume.incibe.sesion01;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class S402_HashProvider {
	public static void main(String[] args) throws Exception {
		/* Cargar "provider" (sólo si no se usa el que viene por defecto) */
		Security.addProvider(new BouncyCastleProvider());  // Usa provider BC
		
		/* Texto para calcular el valor hash */
		String s="Hello World";
		
		System.out.println("----------Usando JCA------------");
 	    MessageDigest messageDigest = MessageDigest.getInstance("SHA256"); // Usar SHA-1
		byte[] buffer = s.getBytes();
		messageDigest.update(buffer, 0,s.length()); 
		byte[] resumen = messageDigest.digest(); // 
		
		System.out.write(resumen, 0, resumen.length);
		System.out.println();
		System.out.println(toHexString(resumen));
		
		System.out.println("----------Usando BouncyCastle------------");
		messageDigest = MessageDigest.getInstance("SHA256","BC"); // Usar SHA-1
		buffer = s.getBytes();
		messageDigest.update(buffer, 0,s.length()); // Pasa texto de entrada a la función resumen
		resumen = messageDigest.digest(); // 
		
		System.out.write(resumen, 0, resumen.length);
		System.out.println();
		System.out.println(toHexString(resumen));


		System.out.println("----------Usando DigestUtils Apache------------");
		byte[] resumenDigestUtils = DigestUtils.sha256(s.getBytes());
		System.out.write(resumenDigestUtils, 0, resumenDigestUtils.length);
		System.out.println();
		String resume=DigestUtils.sha256Hex(s);
		System.out.println(resume);
		
		System.out.println("----------Usando Guava Google------------");
		
		HashCode resumenGuava =  Hashing.sha256()
				  .hashString(s, StandardCharsets.UTF_8);
		System.out.println(resumenGuava);
		
	}
	public static String toHexString(byte[] bytes) {
	    StringBuilder hexString = new StringBuilder();

	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }

	    return hexString.toString();
	}

}
