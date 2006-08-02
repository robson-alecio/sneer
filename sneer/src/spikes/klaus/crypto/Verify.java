package spikes.klaus.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class Verify {
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

	public static void main(String[] args) {
		try {
			File filePublic = new File(args[0]);
			File fileMessage = new File(args[1]);
			File fileSignature = new File(args[2]);

			PublicKey publickey = (PublicKey) KeyTools.readFromFile(filePublic);

			FileInputStream fileinputstream = new FileInputStream(fileMessage);

			byte[] signature = SignatureTools.readFromFile(fileSignature);

			if (verifySignature(publickey, fileinputstream, signature)) {
				System.out.println("true");
			} else {
				System.out.println("false");
			}

			fileinputstream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
