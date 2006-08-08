package spikes.klaus.crypto;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.Provider.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

	static final String SIGNATURE_ALGORITHM = "SHA512WITHRSA";

	public static void main(String[] args) throws Exception {

		printProvidersAndServices();
		System.out.println("\n\n");

		byte[] message = new SecureRandom().generateSeed(10000000);
		
		long t0;

		
		MessageDigest digester = MessageDigest.getInstance("SHA-512");
		System.out.println(digester);
		digester.update(message);
		byte[] digest = digester.digest();
		System.out.println(digest.length);
		
		byte[] digest2 = MessageDigest.getInstance("SHA-512").digest(message);
		System.out.println("Same digest: " + Arrays.equals(digest, digest2));
		
		
		t0 = System.currentTimeMillis();
		KeyPair keys = generateKeyPair();
		System.out.println(System.currentTimeMillis() - t0);
		
		t0 = System.currentTimeMillis();
		byte[] signature = generateSignature(keys.getPrivate(), new ByteArrayInputStream(message));
		System.out.println(signature.length + " " + signature);
		System.out.println(System.currentTimeMillis() - t0);
		
		t0 = System.currentTimeMillis();
		boolean ok = verifySignature(keys.getPublic(), new ByteArrayInputStream(message), signature);
		System.out.println(ok);
		System.out.println(System.currentTimeMillis() - t0);

	}

	private static void printProvidersAndServices() {
		HashSet<String> serviceTypes = new HashSet<String>();

		Provider[] provs = Security.getProviders();
		for (Provider prov : provs) {
			System.out.println();
			System.out.println("===========" + prov);
			Set<Service> services = prov.getServices();
			for (Service service : services) {
				String type = service.getType();
				serviceTypes.add(type);
				System.out.println("\n Type: " + type);
				System.out.println(" Algo: " + service.getAlgorithm());
			}
		}

		for (String type : serviceTypes) {
			System.out.println("\n=====Type: " + type);
			for (String alg : Security.getAlgorithms(type)) {
				System.out.println(alg);
			}
		}
	}

	public static byte[] generateSignature(PrivateKey privatekey, InputStream message) throws Exception {
		Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM);
		
		System.out.println("Signature algorithm: " + signer.getAlgorithm());
	
		signer.initSign(privatekey);
	
		int n;
		byte[] buffer = new byte[1000];
	
		while ((n = message.read(buffer)) > -1)
			signer.update(buffer, 0, n);
	
		return signer.sign();
	}

	public static KeyPair generateKeyPair() throws Exception {
		byte[] seed = "KLAUS WUESTEFELD".getBytes("UTF-8");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(seed);

		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
		keypairgenerator.initialize(4096, random);
		
		return keypairgenerator.generateKeyPair();
	}

	public static Key readFromFile(File file) throws ClassNotFoundException,
			IOException {
		FileInputStream fileinputstream = new FileInputStream(file);
		ObjectInputStream objectinputstream = new ObjectInputStream(
				fileinputstream);
		Key key = (Key) objectinputstream.readObject();
		objectinputstream.close();
		return key;
	}

	public static Key readFromStream(InputStream inputstream)
			throws ClassNotFoundException, IOException {
		return (Key) new ObjectInputStream(inputstream).readObject();
	}

	public static void writeToFile(Key key, File file) throws IOException {
		FileOutputStream fileoutputstream = new FileOutputStream(file);
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(
				fileoutputstream);
		objectoutputstream.writeObject(key);
		objectoutputstream.close();
	}

	public static void writeToStream(Key key, OutputStream outputstream)
			throws IOException {
		new ObjectOutputStream(outputstream).writeObject(key);
	}

	public static boolean verifySignature(PublicKey publickey, InputStream message, byte[] signature) throws Exception {
		Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
		verifier.initVerify(publickey);
	
		int n;
		byte[] buf = new byte[1000];
		while ((n = message.read(buf)) > -1)
			verifier.update(buf, 0, n);
	
		return verifier.verify(signature);
	}

}
