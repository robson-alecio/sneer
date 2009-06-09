package sneer.software.code.compilers.java.impl;

import static sneer.commons.environments.Environments.my;

import sneer.hardware.cpu.lang.Lang;
import sneer.software.code.compilers.java.CompilationError;

class CompilationErrorImpl implements CompilationError {

	private String _fileName;
	
	private int _lineNumber;
	
	private String _message;
	
	public CompilationErrorImpl(String fileName, int lineNumber, String errorMessage) {
		_fileName = fileName;
		_lineNumber = lineNumber;
		_message = my(Lang.class).strings().trimToNull(errorMessage);
	}

	/* (non-Javadoc)
	 * @see sneer.bricks.compiler.impl.ICompilationError#getLineNumber()
	 */
	public int getLineNumber() {
		return _lineNumber;
	}

	/* (non-Javadoc)
	 * @see sneer.bricks.compiler.impl.ICompilationError#getMessage()
	 */
	public String getMessage() {
		return _message;
	}

	/* (non-Javadoc)
	 * @see sneer.bricks.compiler.impl.ICompilationError#getFileName()
	 */
	public String getFileName() {
		return _fileName;
	}

}
