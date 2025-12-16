package es.unex.cume.incibe.sesion01;
import java.security.Security;

public class S101CipherList {
	public static void main(String[] args) {
		for (String s : Security.getAlgorithms("Cipher")) {
			System.out.println(s);
		}
	}
}
