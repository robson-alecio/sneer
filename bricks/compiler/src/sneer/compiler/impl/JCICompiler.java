package sneer.compiler.impl;

import java.io.File;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.apache.commons.jci.problems.CompilationProblem;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;

import sneer.compiler.CompilerException;
import sneer.compiler.Compiler;
import sneer.compiler.Result;
import sneer.lego.Brick;
import sneer.lego.Startable;
import sneer.log.Logger;

/**
 * Wrapper around commons JCI
 */
public class JCICompiler implements Compiler, Startable {

	@Brick
	private Logger log;
	
	private JavaCompiler compiler;
	
	@Override
	public void start() {
		compiler = new JavaCompilerFactory().createCompiler("javac");
	}

	@Override
	public Result compile(File source, File destination) throws CompilerException {
		log.info("Compiling source folder {} to {}", source, destination);
		String ROOT = "/home/leandro/dev/projects/sneer-TRUNK/bricks/compiler";
		String[] files = new String[]{
				ROOT+"/src/sneer/compiler/tests/sample/SampleBrick.java",
				ROOT+"/src/sneer/compiler/tests/sample/impl/SampleBrickImpl.java",
				};
		CompilationResult result = compiler.compile(files, new FileResourceReader(source), new FileResourceStore(destination));

		logErrorsAndWarnings(result);
		return null;
	}

	private void logErrorsAndWarnings(CompilationResult result) {
		CompilationProblem[] errors = result.getErrors();
		log.error("Found"+errors.length + " errors");
		for (CompilationProblem error : errors) {
			log.error("  "+error.getFileName() +" : "+error.getMessage()+"\n");
		}
	}


}
