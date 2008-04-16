package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.FileUtils;
import sneer.lego.utils.asm.MetaClass;


public class JavaImplDirectoryWalker extends ClassInspectorDirectoryWalker {
	
	public JavaImplDirectoryWalker(File root) {
		super(root);
	}

	@Override
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		File parentFile = metaClass.classFile().getParentFile();
		if(parentFile.getName().equals("impl") && !metaClass.isInterface())
			results.add(metaClass);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		String name = directory.getName();
		boolean skip = name.startsWith(".") || FileUtils.isEmpty(directory);
		if(skip) return false;
		return true;
	}

}