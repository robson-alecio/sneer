package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.FileUtils;
import sneer.lego.utils.asm.MetaClass;


public class JavaInterfaceDirectoryWalker extends ClassInspectorDirectoryWalker {
	
	public JavaInterfaceDirectoryWalker(File root) {
		super(root);
	}

	@SuppressWarnings("unused")
	@Override
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		if(metaClass.isInterface())
			results.add(metaClass);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		String name = directory.getName();
		boolean skip = name.startsWith(".") || "impl".equals(name) || FileUtils.isEmpty(directory);
		if(skip) return false;
		return true;
	}

}