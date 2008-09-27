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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.runtime.name"));
		System.out.println(System.getProperty("java.runtime.version"));
		
		Security.addProvider(new BouncyCastleProvider());
		
		printProvidersAndServices();
		
		byte[] bytecodeDummy = new SecureRandom().generateSeed(10);
		
		testSHA512(bytecodeDummy);
		testRSA(bytecodeDummy);
	}

	private static void printProvidersAndServices() {
		HashSet<String> serviceTypes = new HashSet<String>();

		Provider[] provs = Security.getProviders();
		for (Provider prov : provs) {
			System.out.println();
			System.out.println("Provider: " + prov);
			Set<Service> services = prov.getServices();
			for (Service service : services) {
				String type = service.getType();
				serviceTypes.add(type);
				System.out.println("\n\tType: " + type);
				System.out.println("\tAlgo: " + service.getAlgorithm());
			}
		}

		for (String type : serviceTypes) {
			System.out.println("\nService Type: " + type);
			for (String alg : Security.getAlgorithms(type)) {
				System.out.println(alg);
			}
		}
		}

	private static void testSHA512(byte[] bytecodeDummy) throws Exception {
		//MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		//MessageDigest digester = MessageDigest.getInstance("WHIRLPOOL", "BC");
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "BC");
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
//		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "BC");
		random.setSeed(seed);

//		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
//		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA", "BC");
//		keypairgenerator.initialize(4096, random);
		
		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
		keypairgenerator.initialize(256, random);

		return keypairgenerator.generateKeyPair();
	}

	public static byte[] generateSignature(PrivateKey privatekey, byte[] message) throws Exception {
//		Signature signer = Signature.getInstance("SHA512WITHRSA", "SunRsaSign");
//		Signature signer = Signature.getInstance("SHA512WITHRSA", "BC");
		Signature signer = Signature.getInstance("SHA512WITHECDSA", "BC");
		signer.initSign(privatekey);
	
		signer.update(message);
	
		return signer.sign();
	}

	public static boolean verifySignature(PublicKey publickey, byte[] message, byte[] signature) throws Exception {
//		Signature verifier = Signature.getInstance("SHA512WITHRSA", "SunRsaSign");
//		Signature verifier = Signature.getInstance("SHA512WITHRSA", "BC");
		Signature verifier = Signature.getInstance("SHA512WITHECDSA", "BC");
		verifier.initVerify(publickey);
	
		verifier.update(message);
	
		return verifier.verify(signature);
	}

}
