package spikes.sneer.pulp.deployer.impl.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.software.code.filefilters.java.impl.SimpleFilter;
import spikes.sneer.pulp.deployer.impl.parser.JavaSource;
import spikes.sneer.pulp.deployer.impl.parser.JavaSourceParser;

public class ImplFinder extends SimpleFilter {

	public ImplFinder(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		JavaSource source = new JavaSourceParser(file).parse();
		if(!source.isAccessPublic())
			results.add(file);

	}
}
