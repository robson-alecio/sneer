package sneer.compiler;

import java.util.List;

import sneer.compiler.impl.CompilationError;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();
}
