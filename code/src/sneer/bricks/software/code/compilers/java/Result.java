package sneer.bricks.software.code.compilers.java;

import java.util.List;

import sneer.bricks.software.code.metaclass.MetaClass;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<MetaClass> compiledClasses();
}
