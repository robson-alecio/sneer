package sneer.bricks.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.bricks.deployer.impl.parser.JavaSource;
import sneer.bricks.deployer.impl.parser.JavaSourceParser;
import sneer.lego.utils.io.SimpleFilter;

public class InterfaceFinder extends SimpleFilter {

	public InterfaceFinder(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		JavaSource source = new JavaSourceParser(file).parse();
		if(source.isInterface() /* && source.isAssignableTo(Brick.class) */)
			results.add(file);
	}
	
	@Override
	protected String[] ignoreDirectoryNames() {
		return new String[]{".", "impl"};
	}

}
