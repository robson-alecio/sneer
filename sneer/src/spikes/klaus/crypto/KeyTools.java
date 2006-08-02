package spikes.klaus.crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class KeyTools {
	public static void writeToFile(Key key, File file) throws IOException {
		FileOutputStream fileoutputstream = new FileOutputStream(file);
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(
				fileoutputstream);
		objectoutputstream.writeObject(key);
		objectoutputstream.close();
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

	public static void writeToStream(Key key, OutputStream outputstream)
			throws IOException {
		new ObjectOutputStream(outputstream).writeObject(key);
	}

	public static Key readFromStream(InputStream inputstream)
			throws ClassNotFoundException, IOException {
		return (Key) new ObjectInputStream(inputstream).readObject();
	}

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");

		keypairgenerator.initialize(4096, new SecureRandom());

		return keypairgenerator.generateKeyPair();
	}

	public static void main(String[] args) {
		try {
			File filePublic = new File(args[0]);
			File filePrivate = new File(args[1]);

			KeyPair keypair = generateKeyPair();

			writeToFile(keypair.getPublic(), filePublic);
			writeToFile(keypair.getPrivate(), filePrivate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
