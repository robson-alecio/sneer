package sneer.pulp.classpath.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import sneer.pulp.classpath.Classpath;

abstract class ClasspathSupport implements Classpath {

	protected static final File RT_JAR = new File(System.getProperty("java.home")+File.separator+"lib"+File.separator+"rt.jar"); 

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
		result = StringUtils.chomp(result, File.pathSeparatorChar+"");
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
