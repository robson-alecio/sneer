package sneer.pulp.compiler.impl;

import org.apache.commons.lang.StringUtils;

import sneer.pulp.compiler.CompilationError;

public class CompilationErrorImpl implements CompilationError {

	private String _fileName;
	
	private int _lineNumber;
	
	private String _message;
	
	public CompilationErrorImpl(String fileName, int lineNumber, String errorMessage) {
		_fileName = fileName;
		_lineNumber = lineNumber;
		_message = StringUtils.trimToNull(errorMessage);
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
