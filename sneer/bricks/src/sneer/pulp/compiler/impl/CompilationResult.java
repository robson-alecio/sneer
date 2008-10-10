package sneer.pulp.compiler.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import sneer.pulp.compiler.CompilationError;
import sneer.pulp.compiler.CompilerException;
import sneer.pulp.compiler.Result;
import wheel.io.codegeneration.JavaFilter;
import wheel.io.codegeneration.MetaClass;
import wheel.lang.StringUtils;

class CompilationResult implements Result {

	private int _compilerCode;
	
	private String _errorString;
	
	private List<CompilationError> _errors;
	
	private File _targetFolder;
	
	public CompilationResult(int compilerCode, File targetFolder) {
		_compilerCode = compilerCode;
		_targetFolder = targetFolder;
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

	private List<CompilationError> parseErrorString(String string) throws IOException {
		List<CompilationError> result = new ArrayList<CompilationError>();
		List<String> lines = IOUtils.readLines(new StringReader(string));
		for (String line : lines) {
			if(line.indexOf(".java") > 0) {
				final String[] parts = StringUtils.splitRight(line, ':', 3);
				final String fileName = parts[0];
				final int lineNumber = Integer.parseInt(parts[1]);
				final String message = parts[2];
				result.add(new CompilationErrorImpl(fileName, lineNumber, message));
			}
		}
		return result;
	}

	@Override
	public String getErrorString() {
		return _errorString;
	}

	@Override
	public List<MetaClass> compiledClasses() {
		JavaFilter filter = new JavaFilter(_targetFolder);
		return filter.listMetaClasses();
	}
}