package sneer.pulp.classpath.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import wheel.io.codegeneration.SimpleFilter;

class BrickApiFilter extends SimpleFilter {

	public BrickApiFilter(File root) {
		super(root, new OrFileFilter(new SuffixFileFilter(".jar"),  DirectoryFileFilter.INSTANCE));
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		if(file.getName().endsWith("-api.jar"))
			results.add(file);
	}	
}