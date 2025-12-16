package es.unex.cume.incibe.sesion01;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;


public class S601_FirmarCertificado {

	private static final String KEYSTORE_TYPE = "pkcs12";
	//keytool -genkeypair -alias RSAkey -keyalg RSA -keysize 2048 -validity 365 -keystore CatedraIncibeCUMe.p12 -storetype PKCS12 -dname "CN=Prueba Certificado, OU=UEx, O=CUMe, L=Merida, ST=Spain, C=SP" -storepass test1234

	private static final String KEYSTORE_FILE = "CatedraIncibeCUMe.p12";
	private static final String KEYSTORE_PASSWORD = "test1234";

	private KeyStore ks;
	private static String alias = null;

	
	public void Configurar() throws Exception {
		//Security.addProvider(new BouncyCastleProvider());
		ks = KeyStore.getInstance(KEYSTORE_TYPE);
		InputStream is = new FileInputStream(KEYSTORE_FILE);
		ks.load(is, KEYSTORE_PASSWORD.toCharArray());
		alias = (String) ks.aliases().nextElement();
		
	}
	
	public PrivateKey getPrivateKey() throws Exception, KeyStoreException, NoSuchAlgorithmException {
		PrivateKey key = (PrivateKey) ks.getKey(alias,
				KEYSTORE_PASSWORD.toCharArray());
		return key;
	}
	
	public PublicKey getPublicKey() throws KeyStoreException {
		return (PublicKey) ks.getCertificate(alias).getPublicKey();		
	}
	
	public void saveSignature(byte[] realSig, String fSignature) throws Exception {
		/* save the signature in a file */
		FileOutputStream sigfos = new FileOutputStream(fSignature);
		sigfos.write(realSig);
		sigfos.close();
	}
	public byte[] loadSignature(String fSignature) throws Exception {
		FileInputStream sigfis = new FileInputStream(fSignature);
		byte[] sigToVerify = new byte[sigfis.available()];
		sigfis.read(sigToVerify);
		sigfis.close();
		return sigToVerify;
	}


	public byte[] sign(byte[] texto, PrivateKey pKey) throws Exception {

		Signature sig = Signature.getInstance("SHA1withRSA");
		// Inicia el proceso de firma con la clave privada
		sig.initSign(pKey);
		// Actualiza el texto a firmar
		sig.update(texto);
		// Firma el texto pasado previamente
		byte[] signature = sig.sign();
		return signature;
	}


	public boolean verifySign(byte[] textoOriginal, byte[] firma, PublicKey pubKey) throws Exception {
		Signature sig = Signature.getInstance("SHA1withRSA");
		// Procedemos a verificar la firma
		sig.initVerify(pubKey);
		// Aporta el texto original
		sig.update(textoOriginal);
		try {// Verifica la firma con la realizada previamente
			if (sig.verify(firma)) {
				System.out.println("OK");
				return true;
			} else {
				System.out.println("Fallo la verificacion");
			}

		} catch (SignatureException se) {
			System.out.println("Fallo la verificacion");
		}
		return false;

	}


	public boolean signFile(String file, PrivateKey pKey) throws Exception {
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initSign(pKey);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bufin = new BufferedInputStream(fis);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bufin.read(buffer)) >= 0) {
			sig.update(buffer, 0, len);
		}

		bufin.close();
		byte[] realSig = sig.sign();

		// Save signature
		FileOutputStream sigfos = new FileOutputStream(file + ".sign");
		sigfos.write(realSig);
		sigfos.close();
		return true;
	}


	public boolean fileIsValid(String fileOriginal, String FileSign, PublicKey pubKey) {
		boolean verifies = false;
		try {
			FileInputStream sigfis = new FileInputStream(FileSign);
			byte[] sigToVerify = new byte[sigfis.available()];
			sigfis.read(sigToVerify);
			sigfis.close();

			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(pubKey);

			FileInputStream datafis = new FileInputStream(fileOriginal);
			BufferedInputStream bufin = new BufferedInputStream(datafis);

			byte[] buffer = new byte[1024];
			int len;
			while (bufin.available() != 0) {
				len = bufin.read(buffer);
				sig.update(buffer, 0, len);
			}

			bufin.close();

			verifies = sig.verify(sigToVerify);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return verifies;
	}

	public static void main(String[] args) throws Exception {
		S601_FirmarCertificado  ej= new S601_FirmarCertificado ();
		ej.Configurar();
		PrivateKey pk=ej.getPrivateKey();

		ej.signFile("el_quijote.txt", pk);
		System.out.println("Firmado");
	}

}
