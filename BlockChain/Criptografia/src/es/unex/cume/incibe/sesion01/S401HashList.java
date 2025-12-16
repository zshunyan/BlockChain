package es.unex.cume.incibe.sesion01;
import java.security.Security;
public class S401HashList {
	public static void main(String[] args) {
		for (String s : Security.getAlgorithms("MessageDigest")) {
			System.out.println(s);
		}
	}
}
