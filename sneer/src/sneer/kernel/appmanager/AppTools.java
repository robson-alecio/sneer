package sneer.kernel.appmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import sneer.SneerDirectories;
import wheel.io.Jars;

public class AppTools {
	
	private static final String APP_UID_SUFFIX = ".appUID";
	static final int _BUFFER = 2048;
	
	private AppTools(){}
	
	public static void unzip(File source, File target) throws ZipException, IOException{
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        ZipEntry entry;
        ZipFile zipfile = new ZipFile(source);
        Enumeration<?> e = zipfile.entries();
        while(e.hasMoreElements()) {
           entry = (ZipEntry) e.nextElement();
           
           File targetFile = new File(target.getPath(),entry.getName().replaceAll("\\\\", "/"));
           System.out.println("Extracting: " +targetFile.getAbsolutePath());
           if (entry.isDirectory()){
        	   targetFile.mkdirs();
        	   targetFile.setLastModified(entry.getTime());
        	   continue;
           }
           is = new BufferedInputStream(zipfile.getInputStream(entry));
           int count;
           byte data[] = new byte[_BUFFER];
           targetFile.getParentFile().mkdirs();
           FileOutputStream fos = new FileOutputStream(targetFile);
           dest = new BufferedOutputStream(fos, _BUFFER);
           while ((count = is.read(data, 0, _BUFFER)) != -1) 
              dest.write(data, 0, count);
           dest.flush();
           dest.close();
           is.close();
           targetFile.setLastModified(entry.getTime());
        }
	}

	public static void zip(File sourceDir, File targetFile) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile));
		zipDir(sourceDir,sourceDir, zos);
		zos.close();
	}

	private static void zipDir(File rootDir, File dir2zip, ZipOutputStream zos) throws IOException { 
       String[] dirList = dir2zip.list(); 
       byte[] readBuffer = new byte[2156]; 
       int bytesIn = 0; 
       for(int i=0; i<dirList.length; i++) { 
           File f = new File(dir2zip, dirList[i]); 
           if (f.getName().startsWith(".")) //ignore special directories like .svn
        	   continue;
           if (f.isDirectory()) { 
           	zipDir(rootDir, f, zos); 
           	continue; 
           } 
           FileInputStream fis = new FileInputStream(f); 
           ZipEntry anEntry = new ZipEntry(f.getPath().substring(rootDir.getPath().length()+1)); 
           zos.putNextEntry(anEntry); 
           while((bytesIn = fis.read(readBuffer)) != -1) 
               zos.write(readBuffer, 0, bytesIn); 
           fis.close(); 
       } 
	}
	
	public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }
	
	public static void copyRecursive(File sourceDir, File targetDir) throws IOException{
		List<File> files = new ArrayList<File>();
		listFiles(files,sourceDir);
		
		for(File file:files){
			File targetDirectory = new File(targetDir,file.getParentFile().getAbsolutePath().substring(sourceDir.getAbsolutePath().length()));
			targetDirectory.mkdirs();
			copy(file,new File(targetDirectory,file.getName()));
		}
	}
	
	public static void listFiles(List<File> list, File file){
		if (file.isDirectory()){
			for(File child:file.listFiles())
				listFiles(list, child);
		}else{
			list.add(file);
		}
	}
	
	
	public static File findFile(File file, FilenameFilter filter){
		if (file.isDirectory())
			for(File temp:file.listFiles()){
				File result = findFile(temp,filter);
				if (result!=null)
					return result;
			}
		return (filter.accept(file.getParentFile(), file.getName()))?file:null;
	}
	
	public static String pathToPackage(File root, File target){
		String name = target.getAbsolutePath().substring(root.getAbsolutePath().length()+1);
		name = name.replaceAll("/","\\." );
		name = name.replaceAll("\\\\","\\." );
		return name;
	}
	
	public static File findApplicationSource(File directory){
		return AppTools.findFile(directory, new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.equals("Application.java");
			}
		});
	}
	
	public static File findApplicationClass(File directory){
		return AppTools.findFile(directory, new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.equals("Application.class");
			}
		});
	}
	
	public static File findAppUID(File directory){
		return AppTools.findFile(directory, new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.endsWith(APP_UID_SUFFIX);
			}
		});
	}
	
	public static File urlToFile(URL url) {
        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            try {
                uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            } catch (URISyntaxException e1) {
                throw new IllegalArgumentException("broken URL: " + url);
            }
        }
        return new File(uri);
    }
	
	public static File tryToFindSneerLocation() throws IOException {
		try{
			URL url = Jars.jarGiven(AppTools.class);
			return urlToFile(url);
		}catch(Exception e){
			File eclipseProjectRoot = new File("."); //fallback. if it is not running inside jar, try to find jar from bin directory.
			File result = firstJarInDirectory(new File(eclipseProjectRoot,"bin"));
			if (result==null)
				throw new IOException("Could not find SneerXXXX.jar in eclipse bin directory");
			return result;
		}
	}

	private static File firstJarInDirectory(File directory) {
		for(File file:directory.listFiles()){
			if (file.getName().endsWith(".jar"))
				return file;
		}
		return null;
	}
	
	public static String generateAppUID(File jarFile) throws Exception{
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		FileOutputStream out = new FileOutputStream(new File(jarFile.getParentFile(),jarFile.getName()+APP_UID_SUFFIX));
		recursiveDigest(jarFile,digester);
		String appUID = byteArrayToHexString(digester.digest());
		out.write(appUID.getBytes());
		out.close();
		return appUID;
	}
	
	//Fix: attention... a different order of file list can change the result? platform dependant?????
	private static void recursiveDigest(File file, MessageDigest digester) throws Exception{
		if (file.isDirectory())
			for(File child:file.listFiles())
				recursiveDigest(child, digester);
		else
			if (file.getName().endsWith(".java"))
				digester.update(getBytesFromFile(file));
	}
	
	public static String readAppUID(File jarFile) throws Exception{
		return new String(getBytesFromFile(new File(jarFile.getParentFile(),jarFile.getName()+APP_UID_SUFFIX)));
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        is.close();
        return bytes;
    }

	public synchronized static String uniqueName(String prefix){
		return prefix + "-" + System.currentTimeMillis() + "-" + System.nanoTime();
	}
	
	public static File createTempDirectory(String prefix){
		File temp = new File(SneerDirectories.temporaryDirectory(), AppTools.uniqueName(prefix));
		temp.mkdirs();
		return temp;
	}
	
	public static void cleanupTempDirectory(){
		removeRecursive(SneerDirectories.temporaryDirectory());
		SneerDirectories.temporaryDirectory().mkdirs();
	}
	
	public static void removeRecursive(File file){
		if ((file!=null)&&(file.exists()))
			if (file.isDirectory())
				for(File children:file.listFiles())
					removeRecursive(children);
			file.delete();
	}
	
	private static final String hexDigits = "0123456789abcdef";

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer buf = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				int j = b[i] & 0xFF; 
				buf.append(hexDigits.charAt(j / 16)); 
				buf.append(hexDigits.charAt(j % 16)); 
			}   
		return buf.toString();
	}

	public static byte[] hexStringToByteArray(String hexa) throws IllegalArgumentException {
    	if (hexa.length() % 2 != 0) throw new IllegalArgumentException("Invalid Hex String");  
    		byte[] b = new byte[hexa.length() / 2];
    			for (int i = 0; i < hexa.length(); i+=2) {
    				b[i / 2] = (byte) ((hexDigits.indexOf(hexa.charAt(i)) << 4) | (hexDigits.indexOf(hexa.charAt(i + 1))));          
        }
		return b;
	}
	
}
