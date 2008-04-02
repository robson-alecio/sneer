package sneer.bricks.compiler;

import java.io.File;

public interface Compiler {
	
	Result compile(File source, File destination) throws CompilerException;

	Result compile(File source, File destination, File libDir) throws CompilerException;

}
