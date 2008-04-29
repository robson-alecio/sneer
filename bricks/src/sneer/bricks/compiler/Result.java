package sneer.bricks.compiler;

import java.util.List;

import sneer.lego.utils.metaclass.MetaClass;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<MetaClass> compiledClasses();
}
