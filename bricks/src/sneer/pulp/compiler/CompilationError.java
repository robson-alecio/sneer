package sneer.pulp.compiler;

public interface CompilationError {

	int getLineNumber();

	String getMessage();

	String getFileName();

}