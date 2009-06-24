package sneer.bricks.software.code.compilers.java.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.software.code.compilers.java.CompilationError;
import sneer.bricks.software.code.compilers.java.CompilerException;
import sneer.bricks.software.code.compilers.java.Result;
import sneer.bricks.software.code.filefilters.java.JavaFilter;
import sneer.bricks.software.code.filefilters.java.JavaFilters;
import sneer.bricks.software.code.metaclass.MetaClass;

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
		List<String> lines = my(Lang.class).strings().readLines(string);
		for (String line : lines) {
			if(line.indexOf(".java") > 0) {
				final String[] parts = my(Lang.class).strings().splitRight(line, ':', 3);
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
		JavaFilter filter = my(JavaFilters.class).newInstance(_targetFolder);
		return filter.listMetaClasses();
	}
}