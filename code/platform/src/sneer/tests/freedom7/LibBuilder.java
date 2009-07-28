package sneer.tests.freedom7;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.bricks.software.code.compilers.java.JavaCompiler;
import sneer.bricks.software.code.compilers.java.Result;
import sneer.bricks.software.code.jar.JarBuilder;
import sneer.bricks.software.code.jar.Jars;
import sneer.bricks.software.code.metaclass.MetaClass;

public class LibBuilder {

	private final JavaCompiler _compiler;
	private final File _srcFolder;
	private File _tmpFolder;

	public LibBuilder(JavaCompiler compiler, File srcFolder, File tmpFolder) {
		_compiler = compiler;
		_srcFolder = srcFolder;
		_tmpFolder = tmpFolder;
	}

	public void build(File targetJar) throws IOException {
		_tmpFolder.mkdirs();
		
		final Result result = _compiler.compile(my(Iterables.class).toList(iterateSourceFiles()), _tmpFolder);
		if (!result.success())
			throw new IllegalArgumentException(result.getErrorString());
		
		targetJar.getParentFile().mkdirs();
		
		final JarBuilder jar = my(Jars.class).builder(targetJar);
		try {
			for (MetaClass klass : result.compiledClasses()) {
				jar.add(klass.getName().replace('.', '/') + ".class", klass.classFile());
			}
		} finally {
			jar.close();
		}
	}

	private Iterator<File> iterateSourceFiles() {
		return my(IO.class).files().iterate(_srcFolder, new String[] { "java" }, true);
	}

}
