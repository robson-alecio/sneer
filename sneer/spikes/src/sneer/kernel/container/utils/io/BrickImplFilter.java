package sneer.kernel.container.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.software.code.metaclass.MetaClass;

@SuppressWarnings("deprecation")
public class BrickImplFilter extends JavaFilterImpl {
	
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