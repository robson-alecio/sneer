package sneer.kernel.container.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.software.code.metaclass.MetaClass;

@SuppressWarnings("deprecation")
public class BrickInterfaceFilter extends JavaFilterImpl {
	
	public BrickInterfaceFilter(File root) {
		super(root);
	}

	@SuppressWarnings("unused")
	@Override
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		if(metaClass.isInterface())
			results.add(metaClass);
	}

	@Override
	protected String[] ignoreDirectoryNames() {
		return new String[]{".", "impl"};
	}
}