package sneer.software.code.filefilters.java.impl;

import java.io.File;

import sneer.software.code.filefilters.java.JavaFilter;
import sneer.software.code.filefilters.java.JavaFilters;

class JavaFiltersImpl implements JavaFilters {

	@Override
	public JavaFilter newInstance(File root) {
		return new JavaFilterImpl(root);
	}

}
