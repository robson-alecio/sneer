package sneer.software.compilers.java;

public interface CompilationError {

	int getLineNumber();

	String getMessage();

	String getFileName();

}