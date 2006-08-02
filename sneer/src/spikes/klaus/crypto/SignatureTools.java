package spikes.klaus.crypto;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class SignatureTools {
	public static void writeToFile(byte[] rgb, File file) throws IOException {
		RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
		randomaccessfile.write(rgb);
		randomaccessfile.close();
	}

	public static byte[] readFromFile(File file) throws ClassNotFoundException,
			IOException {
		RandomAccessFile randomaccessfile = new RandomAccessFile(file, "r");
		int n = (int) randomaccessfile.length();
		byte[] rgb = new byte[n];
		randomaccessfile.readFully(rgb);
		randomaccessfile.close();
		return rgb;
	}
}
