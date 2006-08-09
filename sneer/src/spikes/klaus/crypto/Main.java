package spikes.klaus.crypto;

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
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws Exception {
		printProvidersAndServices();
		
		byte[] bytecodeDummy = new SecureRandom().generateSeed(10000000);
		
		testSHA512(bytecodeDummy);
		testRSA(bytecodeDummy);
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

	private static void testSHA512(byte[] bytecodeDummy) throws Exception {
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		byte[] digest = digester.digest(bytecodeDummy);
		System.out.println("Digest length: " + digest.length * 8 + " bits");
	}

	private static void testRSA(byte[] bytecodeDummy) throws Exception {
		KeyPair keys = generateKeyPair();
		byte[] signature = generateSignature(keys.getPrivate(), bytecodeDummy);
		boolean ok = verifySignature(keys.getPublic(), bytecodeDummy, signature);
		System.out.println("RSA Signature ok: " + ok);
	}

	public static KeyPair generateKeyPair() throws Exception {
		byte[] seed = "SENHA SECRETA COMPRIDA".getBytes("UTF-8");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		random.setSeed(seed);

		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
		keypairgenerator.initialize(4096, random);
		
		return keypairgenerator.generateKeyPair();
	}

	public static byte[] generateSignature(PrivateKey privatekey, byte[] message) throws Exception {
		Signature signer = Signature.getInstance("SHA512WITHRSA", "SunRsaSign");
		signer.initSign(privatekey);
	
		signer.update(message);
	
		return signer.sign();
	}

	public static boolean verifySignature(PublicKey publickey, byte[] message, byte[] signature) throws Exception {
		Signature verifier = Signature.getInstance("SHA512WITHRSA", "SunRsaSign");
		verifier.initVerify(publickey);
	
		verifier.update(message);
	
		return verifier.verify(signature);
	}

}
