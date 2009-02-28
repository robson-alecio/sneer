package sneer.pulp.classpath.impl;

import java.io.File;

import sneer.pulp.classpath.Classpath;

class ComposedClasspath implements Classpath {

	private Classpath _cp1;
	
	private Classpath _cp2;
	
	public ComposedClasspath(Classpath cp1, Classpath cp2) {
		if(cp1 == null || cp2 == null)
			throw new IllegalArgumentException("Can't compose classpaths");
		
		_cp1 = cp1;
		_cp2 = cp2;
	}

	@Override
	public String asJavacArgument() {
		String fromCp1 = _cp1.asJavacArgument();
		String fromCp2 = _cp2.asJavacArgument();
		return fromCp1 + File.pathSeparator + fromCp2;
	}

	@Override
	public Classpath compose(Classpath other) {
		return new ComposedClasspath(this, other);
	}

	@Override
	public void add(File element) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet();
	}
}
