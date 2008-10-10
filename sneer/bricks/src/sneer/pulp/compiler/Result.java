package sneer.pulp.compiler;

import java.util.List;

import wheel.io.codegeneration.MetaClass;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<MetaClass> compiledClasses();
}
