package sneer.pulp.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.pulp.deployer.impl.parser.JavaSource;
import sneer.pulp.deployer.impl.parser.JavaSourceParser;
import wheel.io.codegeneration.SimpleFilter;

public class ApiSourceFileFinder extends SimpleFilter {

	public ApiSourceFileFinder(File root) {
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
