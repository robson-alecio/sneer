package sneer.bricks.software.code.metaclass.asm.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;

import sneer.bricks.software.code.metaclass.asm.AnnotationInfo;
import sneer.bricks.software.code.metaclass.asm.AnnotationVisitor;
import sneer.bricks.software.code.metaclass.asm.ClassVisitor;
import sneer.bricks.software.code.metaclass.asm.Visitor;

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

class VisitorAdapter extends EmptyVisitor{
	
	private sneer.bricks.software.code.metaclass.asm.Visitor _delegate;

	public VisitorAdapter(Visitor visitor) {
		_delegate = visitor;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		if(!(_delegate instanceof ClassVisitor)) return;
		ClassVisitor classVisitor = (ClassVisitor) _delegate;
		classVisitor.visit(version, access, name, signature, superName, interfaces);
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public org.objectweb.asm.AnnotationVisitor visitAnnotation(String typeSignature, boolean visible) {
		org.objectweb.asm.AnnotationVisitor result = super.visitAnnotation(typeSignature, visible);
		
		if(_delegate instanceof sneer.bricks.software.code.metaclass.asm.AnnotationVisitor){
			AnnotationVisitor visitor = (AnnotationVisitor) _delegate;
			visitor.visit(typeSignature, visible, new AnnotationInfoImpl(result));
		}
		return result;
	}
}

class AnnotationInfoImpl implements AnnotationInfo{

	@SuppressWarnings("unused")
	private final org.objectweb.asm.AnnotationVisitor _result;

	public AnnotationInfoImpl(org.objectweb.asm.AnnotationVisitor result) {
		_result = result;
	}
	
}