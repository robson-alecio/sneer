package sneer.bricks.software.code.metaclass.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public interface ASMVisitor extends ClassVisitor{
	MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4);
	AnnotationVisitor visitAnnotation(String arg0, boolean arg1);
	FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4);

	void visitAttribute(Attribute attribute);
	void visitInnerClass(String arg0, String arg1, String arg2, int arg3) ;
	void visitOuterClass(String arg0, String arg1, String arg2);
	void visitSource(String arg0, String arg1) ;
	void visitEnd();
}
