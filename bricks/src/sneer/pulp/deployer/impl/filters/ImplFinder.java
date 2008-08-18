package sneer.pulp.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.lego.utils.io.SimpleFilter;
import sneer.pulp.deployer.impl.parser.JavaSource;
import sneer.pulp.deployer.impl.parser.JavaSourceParser;

public class ImplFinder extends SimpleFilter {

	public ImplFinder(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
//		File implDir = findImplDir(file);
//		if(implDir == null)
//			return;

		JavaSource source = new JavaSourceParser(file).parse();
		if(/* !source.isInterface() && */ !source.isAccessPublic())
			results.add(file);

	}
	
//	private File findImplDir(File file) {
//		File parent = file.getParentFile();
//		
//		if(parent == null) 
//			return null;
//		
//		if("impl".equals(parent.getName())) {
//			return parent;
//		}
//		return findImplDir(parent);
//		
//	}
}
