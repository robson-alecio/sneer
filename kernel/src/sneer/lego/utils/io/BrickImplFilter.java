package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.metaclass.MetaClass;


public class BrickImplFilter extends JavaFilter {
	
	public BrickImplFilter(File root) {
		super(root);
	}

	@SuppressWarnings("unused")
	@Override
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		File parentFile = metaClass.classFile().getParentFile();
		if(parentFile.getName().equals("impl") /* && !metaClass.isInterface() */)
			results.add(metaClass);
	}
}