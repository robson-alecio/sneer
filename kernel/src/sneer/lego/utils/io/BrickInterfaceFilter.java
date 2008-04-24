package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.asm.IMetaClass;


public class BrickInterfaceFilter extends JavaFilter {
	
	public BrickInterfaceFilter(File root) {
		super(root);
	}

	@SuppressWarnings("unused")
	@Override
	protected void handleClass(IMetaClass metaClass, int depth, Collection<IMetaClass> results) throws IOException {
		if(metaClass.isInterface())
			results.add(metaClass);
	}

	@Override
	protected String[] ignoreDirectoryNames() {
		return new String[]{".", "impl"};
	}
}