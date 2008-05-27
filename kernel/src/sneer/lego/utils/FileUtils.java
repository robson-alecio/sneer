package sneer.lego.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import wheel.lang.exceptions.NotImplementedYet;

public class FileUtils {

	public static boolean isEmpty(File file) {
		if(file == null || !file.exists()) 
			return true;
		
		if(file.isDirectory()) {
			String[] children = file.list();
			return children == null || children.length == 0;
		}
		
		//TODO: check file contents
		return false;
	}
	
	public static File concat(File basePath, String path) {
		return concat(basePath.getAbsolutePath(), path);
	}
	
	public static File concat(String basePath, String path) {
		File file = new File(FilenameUtils.concat(basePath, path)); 
		if(!file.exists()) file.mkdirs();
		return file;
	}
	
	public static void cleanDirectory(File directory) {
		try {
			org.apache.commons.io.FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			throw new NotImplementedYet(e);
		}
	}
}
