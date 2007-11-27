package wheel.io.files.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sneer.kernel.appmanager.AppTools;

public class WindowsAndLinuxCompatibility {
	
	private WindowsAndLinuxCompatibility(){
	}
	
	public static File normalizedFile(String normalizedPath){
		String makeSure = normalizePath(normalizedPath);
		String[] paths = makeSure.split("/");
		File parent = null;
		for(String item:paths){
			parent = normalizedFileInDirectory(parent, item);
		}
		return parent;
	}
	
	public static File normalizedFileInDirectory(File parent, String normalizedName){
		String makeSure = normalizePath(normalizedName);
		if (parent == null)
			parent = new File("/");
		File[] files = parent.listFiles();
		if (files == null) files = new File[0];
		
		for (File file : files) {
			if (normalizePath(file.getName()).equals(makeSure))
				return file;
		}
		return new File(parent,normalizedName);
	}
	
	public static String normalizePath(String path){
		return dontAllowRelativePath(path.toLowerCase().replace('\\', '/'));
	}
	
	public static String dontAllowRelativePath(String path){
		while(path.indexOf("..")>-1){
			path.replace("..", ".");
		}
		return path;
	}
	
	public static List<FileInfo> listAll(String directoryPath, boolean appendParentPathName){
		List<File> list = new ArrayList<File>();
		File parent = normalizedFile(directoryPath);
		AppTools.listFiles(list, parent);
		List<FileInfo> result = new ArrayList<FileInfo>();
		for(File file:list){
			String name=appendParentPathName?file.getAbsolutePath():file.getAbsolutePath().substring(parent.getAbsolutePath().length());
			result.add(new FileInfo(name,file.length()));
		}
		return result;
	}
	
	public static List<FileInfo> list(String directoryPath){
		File directory = normalizedFile(directoryPath);
		File[] files = directory.listFiles();
		List<FileInfo> result = new ArrayList<FileInfo>();
		for(File file:files)
			result.add(new FileInfo(file.getAbsolutePath(),file.length()));
		return result;
	}
	
}
