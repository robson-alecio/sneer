package sneer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import sneer.kernel.appmanager.AppTools;
import wheel.io.Log;

public class Build {
	
	private final File _baseDirectory;
	private File _tempDirectory;
	private File _build;
	private File _src;
	private File _lib;

	public Build(){
		String filename = whereAmI();
		if (filename.contains("!"))
			throw new RuntimeException("Cannot use this constructor from a jar executable, use 'Build(File directory)' instead.");
		_baseDirectory = (new File(filename)).getParentFile().getParentFile().getParentFile();
		System.out.println("Base Directory: "+_baseDirectory.getAbsolutePath());
	}

	private String whereAmI() {
		java.net.URL classUrl = getClass().getResource("/"+getClass().getName().replace('.', '/')+".class");
		return classUrl.getFile();
	}
	
	public Build(File baseDirectory){
		_baseDirectory = baseDirectory;
	}
	
	public synchronized File generate(File jarFile) throws BuildFailure{
		try{
			initDirectories();
			unzipAllLibs();
			compileFiles();
			createManifestWithEntryPoint();
			createJar(jarFile);
		}catch(Exception e){
			throw new BuildFailure(e);
		}finally{
			cleanup();
		}
		
		return jarFile;
	}
	
	public class BuildFailure extends Exception{
		public BuildFailure(Throwable cause) {
			super(cause);
		}
		private static final long serialVersionUID = 1L;
	}
	
	private void initDirectories() {
		_tempDirectory = AppTools.createTempDirectory("build");
		_build = new File(_tempDirectory,"jarBuild");
		_lib = new File(_baseDirectory,"lib");
		_src = new File(_baseDirectory,"src");
		if (!_src.exists())
			throw new IllegalStateException("Could not find SRC directory");
		_build.mkdirs();
	}

	private void unzipAllLibs() throws ZipException, IOException {
		if (!_lib.exists()) return;
		List<File> list = new ArrayList<File>();
		AppTools.listFiles(list, _lib);
		for(File file:list)
			if (file.getName().toLowerCase().endsWith(".jar"))
				AppTools.unzip(file, _build);
	}
	
	private void compileFiles() throws CompilationFailure{
		compile(new File(_src,"sneer/Boot.java"),_build.getAbsolutePath(),_src.getAbsolutePath(),_build.getAbsolutePath(),"1.2","1.1","UTF-8");
		compile(new File(_src,"sneer/SneerJockey.java"),_build.getAbsolutePath(),_src.getAbsolutePath(),_build.getAbsolutePath(),"1.6","1.6","UTF-8");
		compile(new File(_src,"sneer/Sneer.java"),_build.getAbsolutePath(),_src.getAbsolutePath(),_build.getAbsolutePath(),"1.6","1.6","UTF-8");
	}
	
	private void createManifestWithEntryPoint() throws IOException{
        FileOutputStream out = new FileOutputStream(new File(_build,"META-INF/MANIFEST.MF"));
        out.write("Manifest-Version: 1.0\n".getBytes());
        out.write("Created-By: 1.6.0 (Sun Microsystems Inc.)\n".getBytes());
        out.write("SplashScreen-Image: sneer/kernel/gui/images/splash.jpg\n".getBytes());
        out.write("Main-Class: sneer.Boot\n".getBytes());
        out.close();
	}
	
	private void createJar(File jarFile) throws IOException{
		FilenameFilter filter = new FilenameFilter(){ public boolean accept(File dir, String name) {
			return !name.toLowerCase().endsWith(".java");
		}};
		AppTools.copyRecursive(_src, _build, filter);
		AppTools.zip(_build, jarFile, filter);
	}
	
	private void cleanup(){
		AppTools.removeRecursive(_tempDirectory);
	}
	
	private void compile(File source, String classpath, String sourcepath, String targetDirectory, String sourceVersion, String targetVersion, String encoding ) throws CompilationFailure{
			System.out.println("Compiling " + source.getName());
			String[] parameters = { "-classpath", classpath, "-sourcepath", sourcepath , "-source", sourceVersion, "-target", targetVersion, "-encoding", encoding,"-d", targetDirectory, source.getAbsolutePath() };
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (com.sun.tools.javac.Main.compile(parameters, new PrintWriter(out))!=0){
				Log.log(out.toString());
				throw new CompilationFailure();
			}
	}

	public class CompilationFailure extends Exception{
		private static final long serialVersionUID = 1L;
	}

	public static void main(String[] args){
		Build build = new Build();
		try {
			build.generate(new File("/tmp/Sneer-build-candidate.jar"));
			System.out.println("Build sucessfull!");
		} catch (BuildFailure e) {
			e.printStackTrace();
		}
		
	}

}
