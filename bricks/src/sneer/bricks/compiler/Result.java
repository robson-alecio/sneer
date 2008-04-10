package sneer.bricks.compiler;

import java.io.File;
import java.util.List;

import sneer.bricks.compiler.impl.CompilationError;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<File> compiledClasses();
}
