package spikes.klaus.crypto;

import java.security.Signature;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SignatureException;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Sign {
//  DSA
//	MD2WITHRSA
//	SHA1WITHRSA
//	NONEWITHDSA
//	MD5ANDSHA1WITHRSA
//	MD5WITHRSA
//	SHA384WITHRSA
//	SHA256WITHRSA
//	SHA1WITHDSA
//	SHA512WITHRSA
	static final String SIGNATURE_ALGORITHM = "SHA512WITHRSA";

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

	public static void main(String[] args) {
		try {
			File filePrivate = new File(args[0]);
			File fileMessage = new File(args[1]);
			File fileSignature = new File(args[2]);

			PrivateKey privatekey = (PrivateKey) KeyTools
					.readFromFile(filePrivate);

			FileInputStream fileinputstream = new FileInputStream(fileMessage);

			byte[] rgb = generateSignature(privatekey, fileinputstream);

			fileinputstream.close();

			SignatureTools.writeToFile(rgb, fileSignature);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
