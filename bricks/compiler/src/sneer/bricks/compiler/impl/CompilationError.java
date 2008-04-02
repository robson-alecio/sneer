package sneer.bricks.compiler.impl;

import org.apache.commons.lang.StringUtils;

public class CompilationError {

	private String _fileName;
	
	private int _lineNumber;
	
	private String _message;
	
	public CompilationError(String fileName, int lineNumber, String errorMessage) {
		_fileName = fileName;
		_lineNumber = lineNumber;
		_message = StringUtils.trimToNull(errorMessage);
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	public String getMessage() {
		return _message;
	}

	public String getFileName() {
		return _fileName;
	}

}
