package sneer.bricks.software.code.compilers.java;

import java.io.File;
import java.util.List;

import sneer.bricks.software.code.compilers.classpath.Classpath;
import sneer.foundation.brickness.Brick;

@Brick
public interface JavaCompiler {
	
	Result compile(List<File> sourceFiles, File destination) throws CompilerException;
	
	Result compile(List<File> sourceFiles, File destination, Classpath classpath) throws CompilerException;
	
}
