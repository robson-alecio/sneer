package sneer.bricks.software.code.compilers.classpath.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.software.code.compilers.classpath.Classpath;

abstract class ClasspathSupport implements Classpath {

	protected List<File> _elements = new ArrayList<File>();
	
	@Override
	public String asJavacArgument() {
		StringBuffer sb = new StringBuffer();
		List<File> elements = elements();
		for (File entry : elements) {
			sb.append(entry.getAbsolutePath());
			if(entry.isDirectory()) sb.append('/');
			sb.append(File.pathSeparatorChar);
		}
		String result = sb.toString();
		result = my(Lang.class).strings().chomp(result, File.pathSeparatorChar+"");
		return result;
	}

	protected List<File> elements() {
		return _elements;
	}

	@Override
	public void add(File element) {
		_elements.add(element);
	}

	@Override
	public Classpath compose(Classpath other) {
		return new ComposedClasspath(this, other);
	}
}
