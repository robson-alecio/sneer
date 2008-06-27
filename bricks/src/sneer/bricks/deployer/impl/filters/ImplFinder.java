package sneer.bricks.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.bricks.deployer.impl.parser.JavaSource;
import sneer.bricks.deployer.impl.parser.JavaSourceParser;
import sneer.lego.utils.io.SimpleFilter;

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
