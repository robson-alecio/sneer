package sneer.bricks.software.code.metaclass.asm.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

class ClassReaderImpl implements sneer.bricks.software.code.metaclass.asm.ClassReader {

	private File _classFile;
	private ClassReader _reader;

	ClassReaderImpl(File classFile) {
		_classFile = classFile;
	}

	@Override
	public void accept(sneer.bricks.software.code.metaclass.asm.Visitor visitor) {
		if(_reader!=null) return;
		
		InputStream is = null;
		try {
			is = new FileInputStream(_classFile);
			_reader = new ClassReader(is);
			_reader.accept(new VisitorAdapter(visitor), 0);
		} catch (IOException e) {
			throw new ASMException("Error reading meta class from: " + _classFile, e);
		} finally {
			try { is.close(); } catch (Throwable ignore) { }
		}
	}
}