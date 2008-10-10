package sneer.pulp.classpath.impl;

import java.io.File;
import java.util.List;

import wheel.io.codegeneration.SimpleFilter;

class FilteredClasspath extends ClasspathSupport {

	private SimpleFilter _filter;
	
	public FilteredClasspath(SimpleFilter filter) {
		_filter = filter;
	}

	@Override
	protected List<File> elements() {
		return _filter.list();
	}
	
}
