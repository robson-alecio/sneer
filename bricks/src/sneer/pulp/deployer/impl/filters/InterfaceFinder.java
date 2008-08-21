package sneer.pulp.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.kernel.container.utils.io.SimpleFilter;
import sneer.pulp.deployer.impl.parser.JavaSource;
import sneer.pulp.deployer.impl.parser.JavaSourceParser;

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
