package sneer;

import java.io.FileOutputStream;


public class Strap extends Boot {

	static void run(String host, int port) throws Exception {
		new Strap(host, port).run();
	}

	private byte[] _mainJarContents;
	
	Strap(String host, int port) {
		_host = host;
		_port = port;
	}
	
	private void run() throws Exception {
		openConnectionToPeer("Strap");
		getCryptoLibrary();
		getCompiler();
		getMain();
	}

	private void getCompiler() {
		downloadCompiler();
		authenticateCompilerWithPK();
		saveCompiler();
	}

	private void saveCompiler() {
		// TODO Auto-generated method stub
		
	}

	private void getCryptoLibrary() {
		downloadCryptoLibrary();
		authenticateCryptoLibraryWithHash();
		saveCryptoLibrary();
	}

	private void saveCryptoLibrary() {
		// TODO Auto-generated method stub
		
	}

	private void getMain() throws Exception {
		downloadMain();
		authenticateMainWithPK();
		compileMain();
		saveMain();
	}

	private void compileMain() {

		
	}

	private void saveMain() throws Exception {
		FileOutputStream out = new FileOutputStream(mainJar());
		try {
			out.write(_mainJarContents, 0, _mainJarContents.length);
		} finally {
			out.close();
		}
	}

	private void downloadMain() throws Exception {
		_mainJarContents = receiveByteArray();
		
	}

	private void authenticateMainWithPK() {
		showNonAuthenticatedMessage();
	}

	private boolean _nonAuthenticatedMessageShown = false;
	private void showNonAuthenticatedMessage() {
		if (_nonAuthenticatedMessageShown) return;
		_nonAuthenticatedMessageShown = true;
		showError("Sneer Alfa-version Warning:\nInstalling non-authenticated code downloaded from your friend.");
	}

	private void downloadCompiler() {
	}

	private void authenticateCompilerWithPK() {
		showNonAuthenticatedMessage();
	}

	private void downloadCryptoLibrary() {
	}

	private void authenticateCryptoLibraryWithHash() {
		showNonAuthenticatedMessage();
	}
	

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

	
//	private static File mainAppSourceFile() {
//	return new File(sneerDirectory(), "MainApplication.zip");
//}

	
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

}
