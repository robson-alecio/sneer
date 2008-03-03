package sneer.compiler;

import java.io.File;

public interface Compiler {
	
	Result compile(File source, File destination)
		throws CompilerException;

}
