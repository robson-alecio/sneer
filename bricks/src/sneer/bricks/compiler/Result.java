package sneer.bricks.compiler;

import java.io.File;
import java.util.List;

public interface Result {

	boolean success();

	void setError(String errorString);

	List<CompilationError> getErrors();

	String getErrorString();

	List<File> compiledClasses();
}
