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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class AppTools {
	
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
           System.out.println("Extracting: " +entry);
           File targetFile = new File(target.getPath()+File.separator+entry.getName());
           if (entry.isDirectory())
           	continue;
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
	
}
