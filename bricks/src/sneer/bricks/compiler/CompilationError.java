package sneer.bricks.compiler;

public interface CompilationError {

	int getLineNumber();

	String getMessage();

	String getFileName();

}