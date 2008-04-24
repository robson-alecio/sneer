package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.asm.IMetaClass;


public class JavaInterfaceFilter extends JavaFilter {
	
	public JavaInterfaceFilter(File root) {
		super(root);
	}

	@SuppressWarnings("unused")
	@Override
	protected void handleClass(IMetaClass metaClass, int depth, Collection<IMetaClass> results) throws IOException {
		if(metaClass.isInterface())
			results.add(metaClass);
	}
}