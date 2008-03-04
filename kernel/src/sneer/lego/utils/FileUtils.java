package sneer.lego.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static boolean isEmpty(File binDir) {
		String[] children = binDir.list();
		return children == null || children.length == 0;
	}
	
	public static File concat(File basePath, String path) {
		return concat(basePath.getAbsolutePath(), path);
	}
	
	public static File concat(String basePath, String path) {
		File file = new File(FilenameUtils.concat(basePath, path)); 
		if(!file.exists()) file.mkdirs();
		return file;
	}


}
