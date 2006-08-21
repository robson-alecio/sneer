package sneer;

public class Strap {

	public static void run() {
		System.out.println("Hello World - By Strap");
	}

//	private File lastValidAppJarFile() {
//	while (true) {
//		File candidate = lastAppJarFile();
//		if (candidate == null) return null;
//		if (isValidSignature(candidate)) return candidate;
//		deleteSignedFile(candidate);
//	}
//	}

//	private static void compileMainApp() throws Exception {
//	delete(tempDirectory());
//	extractMainAppSource();
//	compileMainAppSource();
//	}
	//
//	private static void compileMainAppSource() throws Exception {
//	File dest = new File(tempDirectory(), "classes");
//	dest.mkdir();
	//
//	execute(compilerJar(), 
//		"-source", "1.6",
//		"-target", "1.6",
//		"-d", dest.getAbsolutePath(),
//		sourceDirectory().getAbsolutePath()
//	);
	//
	//
////	FileOutputStream os = new FileOutputStream(jar);
////	JarOutputStream jos = new JarOutputStream(os, manifest());
////	addJarEntries(jar, jos);
////	jos.close();		
	//
//	}


//	private static void delete(File file) throws IOException {
//		if (file.isDirectory()) {
//			for (File subFile : file.listFiles()) delete(subFile);
//			return;
//		}
//		if (!file.delete()) throw new IOException("Unable to delete file " + file);
//	}

//	private static void extractMainAppSource() throws Exception {
//		ZipFile sources = new ZipFile(mainAppSourceFile());
//		Enumeration<? extends ZipEntry> entries = sources.entries();
//		while (entries.hasMoreElements()) {
//			extractMainAppSourceEntry(sources, entries.nextElement());
//		}
//	}

//	private static void extractMainAppSourceEntry(ZipFile sources, ZipEntry entry) throws IOException {
//		if (entry.isDirectory()) return;
//		
//		InputStream inputStream = sources.getInputStream(entry);
//		byte[] buffer = new byte[1024*4];
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		int read;
//		while (-1 != (read = inputStream.read(buffer))) {
//			bos.write(buffer, 0, read);
//		}
//		inputStream.close();
//		
//		File sourceFile = new File(sourceDirectory(), entry.getName());
//		save(sourceFile, bos.toByteArray());
//	}

//	private static File tempDirectory() {
//		File result = new File(sneerDirectory(), "temp");
//		result.mkdir();
//		return result;
//	}



//	private static File firstAppJar() {
//		return new File(appDirectory(), "0000000000.jar");
//	}



//	private static void saveSignedFile(File file, byte[] contents, byte[] signature) throws IOException {
//		File signatureFile = new File(file.getAbsolutePath() + ".signature");
//		save(signatureFile, signature);
//		save(file, contents);
//	}
//		private static File mainAppSourceFile() {
//			return new File(sneerDirectory(), "MainApplication.zip");
//		}
		
//		private static File lastAppJarFile() {
//			File[] versions = appDirectory().listFiles();
	//
//			File result = null;
//			for (File version : versions) {
//				String name = version.getName();
//				if (!name.endsWith(".jar")) continue;
//				if (result == null) result = version;
//				if (name.compareTo(result.getName()) > 0) result = version;
//			}
//			return result;
//		}
//	private static void addJarEntries(File dir, JarOutputStream jos){
//	File files[] = dir.listFiles();
	//
//	for (File file : files) {
//		if(file.isDirectory()){
//			addJarEntries(file, jos);
//		} else {
//			ZipEntry entry = new ZipEntry(resourceName(clazz));	
//			jos.putNextEntry(entry);
//			jos.write(readClassBytes(clazz));
//			jos.closeEntry();			
//		}
//	}		
//	}

//	private static void receiveCompiler() throws Exception {
//	receiveFileContents(compilerJar());
//	}

//	private static File compilerJar() {
//	return new File(sneerDirectory(), "compiler.jar");
//	}

//	private static void receiveFileContents(File file) throws Exception {
//	save(file, receiveByteArray());
//	}
//	private static void save(File file, byte[] contents) throws IOException {
//	FileOutputStream fos = new FileOutputStream(file);
//	try {
//	fos.write(contents);
//	} finally {
//	fos.close();
//	}
//	}

	
}
