package sneer.compiler.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;

import sneer.compiler.Compiler;
import sneer.compiler.CompilerException;
import sneer.compiler.Result;
import sneer.lego.Brick;
import sneer.log.Logger;

import com.sun.tools.javac.Main;

public class CompilerImpl implements Compiler {

	@Brick
	private Logger log;

	@Override
	public Result compile(File source, File destination) throws CompilerException {
		return compile(source, destination, null);
	}
	
	@Override
	public Result compile(File source, File destination, File libDir) throws CompilerException {
		
		List<File> files = buildSourceList(source);
		File tmpFile = createArgsFileForJavac(files);
		log.info("Compiling {} files to {}", files.size(), destination);

		String[] parameters = {
				"-classpath", buildClassPath(libDir),
				"-d", destination.getAbsolutePath(),
				"-encoding","UTF-8",
				"@"+tmpFile.getAbsolutePath()
		};
		log.debug("compiler cli: {}",StringUtils.join(parameters, " "));

		StringWriter writer = new StringWriter();
		int code = Main.compile(parameters, new PrintWriter(writer));
		tmpFile.delete();
		
		Result result = new CompilationResult(code);
		if (code != 0) {
			result.setError(writer.getBuffer().toString());
		}
		return result;
	}

	private File createArgsFileForJavac(List<File> files) {
		try {
			File args = File.createTempFile("javac-", ".args");
			FileUtils.writeStringToFile(args,StringUtils.join(files, "\n"));
			return args;
		} catch(IOException e) {
			throw new CompilerException("Can't create temp file", e);
		}
	}

	private List<File> buildSourceList(File source) {
		JavaDirectoryWalker walker = new JavaDirectoryWalker(source);
		List<File> files;
		try {
			files = walker.list();
		} catch (IOException e) {
			throw new CompilerException("Error building source list", e);
		}
		return files;
	}

	private String buildClassPath(File libDir) {
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("java.home")).append(File.separator).append("lib").append(File.separator).append("rt.jar");
		if(!sneer.lego.utils.FileUtils.isEmpty(libDir)) {
			sb.append(File.pathSeparatorChar);
			File[] libs = libDir.listFiles((FilenameFilter) new SuffixFileFilter(".jar"));
			for (File lib : libs) {
				sb.append(lib.getAbsolutePath());
				sb.append(File.pathSeparatorChar);
			}
		}
		String classPath = sb.toString(); 
		return classPath;
	}
}