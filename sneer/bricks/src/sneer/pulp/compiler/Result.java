package sneer.pulp.compiler;

import java.util.List;

import sneer.kernel.container.utils.metaclass.MetaClass;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<MetaClass> compiledClasses();
}
