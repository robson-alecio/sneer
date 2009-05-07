package functional.freedom7;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import sneer.brickness.testsupport.JarBuilder;
import sneer.software.compilers.java.JavaCompiler;
import sneer.software.compilers.java.Result;
import wheel.io.codegeneration.MetaClass;
import wheel.lang.Collections;

public class LibBuilder {

	private final JavaCompiler _compiler;
	private final File _srcFolder;
	private File _tmpDirectory;

	public LibBuilder(JavaCompiler compiler, File srcFolder, File tmpDirectory) {
		_compiler = compiler;
		_srcFolder = srcFolder;
		_tmpDirectory = tmpDirectory;
	}

	public void build(File targetJar) throws IOException {
		_tmpDirectory.mkdirs();
		
		final Result result = _compiler.compile(Collections.toList(iterateSourceFiles()), _tmpDirectory);
		if (!result.success())
			throw new IllegalArgumentException(result.getErrorString());
		
		targetJar.getParentFile().mkdirs();
		
		final JarBuilder jar = new JarBuilder(targetJar);
		try {
			for (MetaClass klass : result.compiledClasses()) {
				jar.add(klass.getName().replace('.', '/') + ".class", klass.classFile());
			}
		} finally {
			jar.close();
		}
	}

	private Iterator<File> iterateSourceFiles() {
		return FileUtils.iterateFiles(_srcFolder, new String[] { "java" }, true);
	}

}
