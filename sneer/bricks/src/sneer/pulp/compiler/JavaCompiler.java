package sneer.pulp.compiler;

import java.io.File;
import java.util.List;

import sneer.brickness.Brick;
import sneer.pulp.classpath.Classpath;

public interface JavaCompiler extends Brick {
	
	Result compile(List<File> sourceFiles, File destination) throws CompilerException;
	
	Result compile(List<File> sourceFiles, File destination, Classpath classpath) throws CompilerException;
	
}
