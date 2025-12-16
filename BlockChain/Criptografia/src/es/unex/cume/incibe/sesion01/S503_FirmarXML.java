package es.unex.cume.incibe.sesion01;

import java.io.*;
import java.security.*;
import java.util.Base64;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class S503_FirmarXML {
    public static void main(String[] args) throws Exception {
        // 1. Generar claves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // 2. Archivo XML a firmar
        File xmlFile = new File("Cervantes_Quijote1_TEI_XML.xml");

        // 3. Firmar e incrustar la firma en el XML
        File signedXmlFile = new File("Cervantes_Quijote1_TEI_XML_Firma.xml");
        signXML(xmlFile, signedXmlFile, keyPair.getPrivate());

        // 4. Verificar la firma en el XML firmado
        boolean esValida = verifyXMLSignature(signedXmlFile, keyPair.getPublic());
        System.out.println("¿Firma válida? " + esValida);
    }

    public static void signXML(File inputFile, File outputFile, PrivateKey privateKey) throws Exception {
        // Leer XML original
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputFile);

        // Convertir XML a bytes
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        byte[] xmlBytes = outputStream.toByteArray();

        // Crear firma digital
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(xmlBytes);
        byte[] firmaBytes = signature.sign();
        String firmaBase64 = Base64.getEncoder().encodeToString(firmaBytes);

        // Incrustar la firma en el XML
        Element root = doc.getDocumentElement();
        Element signatureElement = doc.createElement("Signature");
        signatureElement.setTextContent(firmaBase64);
        root.appendChild(signatureElement);

        // Guardar XML firmado
        Transformer transformerOutput = TransformerFactory.newInstance().newTransformer();
        transformerOutput.transform(new DOMSource(doc), new StreamResult(outputFile));
        System.out.println("XML firmado guardado en: " + outputFile.getAbsolutePath());
    }

    public static boolean verifyXMLSignature(File inputFile, PublicKey publicKey) throws Exception {
        // Leer XML firmado
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputFile);

        // Extraer la firma
        NodeList nodeList = doc.getElementsByTagName("Signature");
        if (nodeList.getLength() == 0) {
            throw new Exception("No se encontró firma en el XML.");
        }
        String firmaBase64 = nodeList.item(0).getTextContent();
        byte[] firmaBytes = Base64.getDecoder().decode(firmaBase64);

        // Remover la firma del XML antes de verificar
        Node firmaNodo = nodeList.item(0);
        firmaNodo.getParentNode().removeChild(firmaNodo);

        // Convertir XML a bytes nuevamente
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        byte[] xmlBytes = outputStream.toByteArray();

        // Verificar la firma
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(xmlBytes);
        return signature.verify(firmaBytes);
    }
}
