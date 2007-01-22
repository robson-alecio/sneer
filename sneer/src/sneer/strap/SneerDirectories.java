package sneer.strap;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SneerDirectories {

	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();

	
	private static File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	public static File sneerDirectory() {
		return new File(userHome(), ".sneer");
	}


	static String userHome() {
		return System.getProperty("user.home");
	}

	
	private static String zeroPad(int fileNumber) {
		String concat = SneerDirectories.ZERO_MASK + fileNumber;
		return concat.substring(concat.length() - SneerDirectories.ZERO_MASK.length());
	}

	
	static File logDirectory() {
		return new File(sneerDirectory(), "logs");
	}

	
	static void writeMainAppFile(byte[] contents, int version) throws IOException {
		programsDirectory().mkdir();
		File part = new File(programsDirectory(), "sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(contents);
		fos.close();
		
		part.renameTo(new File(programsDirectory(), PREFIX + zeroPad(version) + SUFFIX));
	}

	
	public static File findNewestMainApp() {
		return findNewestMainApp(programsDirectory());
	}

	
	public static File findNewestMainApp(File directory) {
		System.out.println("Looking for new apps in:" + directory.getAbsolutePath());

		int newest = 0;
		for (String filename : listFilenames(directory))
			if (validNumber(filename) > newest) newest = validNumber(filename);  
		
		if (newest == 0) return null;
		return new File(directory, PREFIX + zeroPad(newest) + SUFFIX);
	}

	
	private static String[] listFilenames(File directory) {
		String[] result = directory.list();
		if (result == null) return new String[0];
		return result;
	}

	
	public static int validNumber(String mainAppCandidate) {
		if (!mainAppCandidate.startsWith(PREFIX)) return -1;
		if (!mainAppCandidate.endsWith(SUFFIX)) return -1;
		if (mainAppCandidate.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainAppCandidate.substring(SneerDirectories.PREFIX.length(), SneerDirectories.PREFIX.length() + SneerDirectories.ZERO_MASK.length()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}


	
}
