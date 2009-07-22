package sneer.bricks.software.bricks.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.software.bricks.Bricks;
import sneer.bricks.software.code.compilers.classpath.Classpath;
import sneer.bricks.software.code.compilers.classpath.ClasspathFactory;
import sneer.bricks.software.code.compilers.java.JavaCompiler;
import sneer.bricks.software.code.compilers.java.Result;
import sneer.bricks.software.code.metaclass.MetaClass;
import sneer.bricks.software.directoryconfig.DirectoryConfig;

public class BricksImpl implements Bricks {

	@Override
	public void install(File sourceDirectory) throws IOException {
		
		final Result result = compile(sourceDirectory, ownBinDirectory());
		if (!result.success())
			throw new CompilationError(result.getErrorString());
		
		copyToOwnSrc(sourceDirectory);
		
		final String brickName = brickNameFor(result);
		if (null == brickName)
			throw new IllegalArgumentException("Directory '" + sourceDirectory + "' contains no bricks.");
		
		instatiate(brickName);
	}

	private void copyToOwnSrc(File sourceDirectory) throws IOException {
		my(IO.class).files().copyDirectory(sourceDirectory, my(DirectoryConfig.class).ownSrcDirectory().get());
	}

	private void instatiate(final String brickName) {
		try {
			my(BricksImpl.class.getClassLoader().loadClass(brickName));
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private String brickNameFor(Result result) {
		for (MetaClass metaClass : result.compiledClasses())
			if (metaClass.isInterface())
				return metaClass.getName();
		return null;
	}

	private Result compile(File sourceDirectory, final File tempOutputDir) {
		final List<File> sourceFiles = sourceFilesIn(sourceDirectory);
		final Classpath classPath = my(ClasspathFactory.class).sneerApi();
		return my(JavaCompiler.class).compile(sourceFiles, tempOutputDir, classPath);
	}

	private List<File> sourceFilesIn(File sourceDirectory) {
		String[] extensions = { "java" };
		return new ArrayList<File>(my(IO.class).files().listFiles(sourceDirectory, extensions, true));
	}

	private File ownBinDirectory() {
		File result = my(DirectoryConfig.class).ownBinDirectory().get();
		if (!result.exists() && !result.mkdirs())
			throw new IllegalStateException("Could not create own brick bin directory: '" + result + "'!");
		return result;
	}

}
