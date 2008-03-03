package sneer.compiler.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import sneer.compiler.CompilerException;
import sneer.compiler.Result;

public class CompilationResult implements Result {

	private int _compilerCode;
	
	private String _errorString;
	
	private List<CompilationError> _errors;
	
	
	public CompilationResult(List<File> files, int compilerCode) {
		_compilerCode = compilerCode;
	}

	@Override
	public boolean success() {
		return _compilerCode == 0;
	}

	@Override
	public void setError(String errorString) {
		_errorString = errorString;
	}

	@Override
	public List<CompilationError> getErrors() {
		if(_errorString != null && _errors == null) {
			try {
				_errors = parseErrorString(_errorString);
			} catch (IOException e) {
				throw new CompilerException("Can't parse error information", e);
			}
		}
		return _errors;
		
	}

	@SuppressWarnings("unchecked")
	private List<CompilationError> parseErrorString(String string) throws IOException {
		List<CompilationError> result = new ArrayList<CompilationError>();
		List<String> lines = IOUtils.readLines(new StringReader(string));
		for (String line : lines) {
			if(line.indexOf(".java") > 0) {
				String[] parts = StringUtils.split(line, ":");
				String fileName = parts[0];
				int lineNumber = Integer.parseInt(parts[1]);
				String message = parts[2];
				result.add(new CompilationError(fileName, lineNumber, message));
			}
		}
		return result;
	}
}
