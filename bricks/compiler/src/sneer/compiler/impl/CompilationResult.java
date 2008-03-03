package sneer.compiler.impl;

import java.io.File;
import java.util.List;

import sneer.compiler.Result;

public class CompilationResult implements Result {

	private int _compilerCode;
	
	public CompilationResult(List<File> files, int compilerCode) {
		_compilerCode = compilerCode;
	}

	@Override
	public boolean success() {
		return _compilerCode == 0;
	}

	@Override
	public void setError(String errorString) {
	}

}
