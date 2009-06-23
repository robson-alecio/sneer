package sneer.bricks.software.code.metaclass.asm.impl;

import org.objectweb.asm.commons.EmptyVisitor;

import sneer.bricks.software.code.metaclass.asm.AnnotationVisitor;
import sneer.bricks.software.code.metaclass.asm.ClassVisitor;
import sneer.bricks.software.code.metaclass.asm.Visitor;

class VisitorAdapter extends EmptyVisitor{
	
	private sneer.bricks.software.code.metaclass.asm.Visitor _delegate;

	public VisitorAdapter(Visitor visitor) {
		_delegate = visitor;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		if(_delegate instanceof ClassVisitor) {
			ClassVisitor classVisitor = (ClassVisitor) _delegate;
			classVisitor.visit(version, access, name, signature, superName, interfaces);
		}
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