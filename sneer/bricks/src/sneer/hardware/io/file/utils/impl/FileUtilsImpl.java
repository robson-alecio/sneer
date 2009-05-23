package sneer.hardware.io.file.utils.impl;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import sneer.hardware.io.file.utils.FileUtils;

class FileUtilsImpl implements FileUtils {

	@Override
	public boolean isEmpty(File file) {
		if(file == null || !file.exists()) 
			return true;

		if(file.isDirectory()) {
			String[] children = file.list();
			return children == null || children.length == 0;
		}

		if(file.length() == 0) return true;

		return false;
	}

	@Override
	public File concat(File basePath, String path) {
		return concat(basePath.getAbsolutePath(), path);
	}

	@Override
	public File concat(String basePath, String path) {
		File file = new File(FilenameUtils.concat(basePath, path)); 
		if(!file.exists()) file.mkdirs();

		return file;
	}
}
