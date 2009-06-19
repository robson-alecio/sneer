package sneer.bricks.software.code.compilers.java.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.software.code.compilers.classpath.Classpath;
import sneer.bricks.software.code.compilers.classpath.ClasspathFactory;
import sneer.bricks.software.code.compilers.java.CompilerException;
import sneer.bricks.software.code.compilers.java.JavaCompiler;
import sneer.bricks.software.code.compilers.java.Result;

import com.sun.tools.javac.Main;

class JavaCompilerImpl implements JavaCompiler {

	private final ClasspathFactory _classpathFactory = my(ClasspathFactory.class);

	@Override
	public Result compile(List<File> sourceFiles, File destination) throws CompilerException {
		return compile(sourceFiles, destination, _classpathFactory.newClasspath());
	}

	@Override
	public Result compile(List<File> sourceFiles, File destination, Classpath classpath) throws CompilerException {
		
		File tmpFile = createArgsFileForJavac(sourceFiles);
//		my(Logger.class).log("Compiling {} files to {}", sourceFiles.size(), destination);

		String[] parameters = {
				"-classpath", classpath.asJavacArgument(),
				"-d", destination.getAbsolutePath(),
				"-encoding","UTF-8",
				"@"+tmpFile.getAbsolutePath()
		};
//		my(Logger.class).log("compiler cli: {}",StringUtils.join(parameters, " "));

		StringWriter writer = new StringWriter();
		int code = Main.compile(parameters, new PrintWriter(writer));
		tmpFile.delete();
		
		Result result = new CompilationResult(code, destination);
		if (code != 0) {
			result.setError(writer.getBuffer().toString());
		}
		return result;
	}

	private File createArgsFileForJavac(List<File> files) {
		try {
			File args = File.createTempFile("javac-", ".args");
			
			my(IO.class).files().writeStringToFile(args, my(Lang.class).strings().join(files, "\n"));
			return args;
		} catch(IOException e) {
			throw new CompilerException("Can't create temp file", e);
		}
	}

//	private List<File> buildSourceList(File source) {
//		JavaDirectoryWalker walker = new JavaDirectoryWalker(source);
//		List<File> files;
//		try {
//			files = walker.list();
//		} catch (IOException e) {
//			throw new CompilerException("Error building source list", e);
//		}
//		return files;
//	}

//	private String buildClassPath(File libDir) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(System.getProperty("java.home")).append(File.separator).append("lib").append(File.separator).append("rt.jar");
//		if(!sneer.lego.utils.FileUtils.isEmpty(libDir)) {
//			sb.append(File.pathSeparatorChar);
//			File[] libs = libDir.listFiles((FilenameFilter) new SuffixFileFilter(".jar"));
//			for (File lib : libs) {
//				sb.append(lib.getAbsolutePath());
//				sb.append(File.pathSeparatorChar);
//			}
//		}
//		String classPath = sb.toString(); 
//		return classPath;
//	}
}