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
import sneer.bricks.software.folderconfig.FolderConfig;

public class BricksImpl implements Bricks {

	@Override
	public void install(File sourceFolder) throws IOException {
		
		final Result result = compile(sourceFolder, ownBinFolder());
		if (!result.success())
			throw new CompilationError(result.getErrorString());
		
		copyToOwnSrc(sourceFolder);
		
		final String brickName = brickNameFor(result);
		if (null == brickName)
			throw new IllegalArgumentException("Folder '" + sourceFolder + "' contains no bricks.");
		
		instatiate(brickName);
	}

	private void copyToOwnSrc(File sourceFolder) throws IOException {
		my(IO.class).files().copyFolder(sourceFolder, my(FolderConfig.class).ownSrcFolder().get());
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

	private Result compile(File sourceFolder, final File tempOutputDir) {
		final List<File> sourceFiles = sourceFilesIn(sourceFolder);
		final Classpath classPath = my(ClasspathFactory.class).sneerApi();
		return my(JavaCompiler.class).compile(sourceFiles, tempOutputDir, classPath);
	}

	private List<File> sourceFilesIn(File sourceFolder) {
		String[] extensions = { "java" };
		return new ArrayList<File>(my(IO.class).files().listFiles(sourceFolder, extensions, true));
	}

	private File ownBinFolder() {
		File result = my(FolderConfig.class).ownBinFolder().get();
		if (!result.exists() && !result.mkdirs())
			throw new IllegalStateException("Could not create own brick bin folder: '" + result + "'!");
		return result;
	}

}
