package spikes.klaus.crypto;

import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.HashSet;
import java.util.Set;

public class Main {

	static final String SIGNATURE_ALGORITHM = "SHA512WITHRSA";

	public static void main(String[] args) {

		printProvidersAndServices();
		System.out.println("\n\n");

		String priv = "c:/temp/privateKey";
		String pub = "c:/temp/publicKey";
		String message = "c:/temp/message";
		String signature = "c:/temp/signature";
		
		long t0;

//		t0 = System.currentTimeMillis();
//		KeyTools.main(new String[]{pub, priv});
//		System.out.println(System.currentTimeMillis() - t0);
		
		t0 = System.currentTimeMillis();
		Sign.main(new String[]{priv, message, signature});
		System.out.println(System.currentTimeMillis() - t0);
		
		t0 = System.currentTimeMillis();
		Verify.main(new String[]{pub, message, signature});
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

	public static byte[] generateSignature(PrivateKey privatekey,
			InputStream inputstreamMessage) throws NoSuchAlgorithmException,
			InvalidKeyException, SignatureException, IOException {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		
		System.out.println("Signature algorithm: " + signature.getAlgorithm());
	
		signature.initSign(privatekey);
	
		int n = 0;
		byte[] rgb = new byte[1000];
	
		while ((n = inputstreamMessage.read(rgb)) > -1) {
			signature.update(rgb, 0, n);
		}
	
		rgb = signature.sign();
	
		return rgb;
	}

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
	
		keypairgenerator.initialize(4096, new SecureRandom(new byte[]{1}));
	
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

	public static boolean verifySignature(PublicKey publickey,
			InputStream inputstreamMessage, byte[] signatureBytes)
			throws NoSuchAlgorithmException, InvalidKeyException,
			SignatureException, IOException {
		Signature signatureAlgorithm = Signature.getInstance(Sign.SIGNATURE_ALGORITHM);
	
		signatureAlgorithm.initVerify(publickey);
	
		int n = 0;
		byte[] buf = new byte[1000];
	
		while ((n = inputstreamMessage.read(buf)) > -1) {
			signatureAlgorithm.update(buf, 0, n);
		}
	
		return signatureAlgorithm.verify(signatureBytes);
	}

}
