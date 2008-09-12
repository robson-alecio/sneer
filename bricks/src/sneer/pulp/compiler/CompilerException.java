package sneer.pulp.compiler;

public class CompilerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CompilerException(String message) {
		super(message);
	}

	public CompilerException(String message, Throwable t) {
		super(message, t);
	}
}
