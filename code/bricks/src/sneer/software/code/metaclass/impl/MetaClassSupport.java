package sneer.software.code.metaclass.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.hardware.io.IO;
import sneer.software.code.metaclass.MetaClass;


abstract class MetaClassSupport implements MetaClass {

	protected File _classFile;

	protected File _root;
	
	protected String _className;

	protected String _packageName;

	protected boolean _isInterface;

	public MetaClassSupport(File root, File classFile) {
		_root = root;
		_classFile = classFile;
		parse();
	}

	protected abstract void parse();

	@Override
	public String getName() {
		return _className;
	}

	@Override
	public String getPackageName() {
		return _packageName;
	}

	@Override
	public boolean isInterface() {
		return _isInterface;
	}
	
	@Override
	public byte[] bytes() throws IOException {
		return my(IO.class).files().readFileToByteArray(_classFile);
	}

	@Override
	public File classFile() {
		return _classFile;
	}
	
	@Override
	public String toString() {
		return _className + " @" + _classFile;
	}

}
