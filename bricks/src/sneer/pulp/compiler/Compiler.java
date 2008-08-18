package sneer.pulp.compiler;

import java.io.File;
import java.util.List;

import sneer.pulp.classpath.Classpath;

public interface Compiler {
	
	Result compile(List<File> sourceFiles, File destination) throws CompilerException;
	
	Result compile(List<File> sourceFiles, File destination, Classpath classpath) throws CompilerException;
	
	/* Fix: Check if we need the methods bellow */
	Result compile(File sourceRoot, File destination) throws CompilerException;
	Result compile(File sourceRoot, File destination, Classpath classpath) throws CompilerException;
}
