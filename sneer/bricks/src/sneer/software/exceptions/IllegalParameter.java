package sneer.software.exceptions;

/** The checked counterpart of java.lang.IllegalArgumentException
 * It is not called simply IllegalArgument because it is easy to confuse them and throw the unchecked IllegalArgumentException causing bugs.
 * 
 * Fix For now, it has been made a RuntimeException because of a bug in the Eclipse compiler . Refactor: Make this a checked Exception again and run all tests once this bug is fixed: https://bugs.eclipse.org/bugs/show_bug.cgi?id=238484 See wheel.lang.Consumer . Eclipse 3.4.0 still has the bug. 
 */
public class IllegalParameter extends Exception {

	public IllegalParameter(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
