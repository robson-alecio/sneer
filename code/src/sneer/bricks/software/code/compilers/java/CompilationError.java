package sneer.bricks.software.code.compilers.java;

public interface CompilationError {

	int getLineNumber();

	String getMessage();

	String getFileName();

}