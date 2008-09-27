package sneer.kernel.container.impl.classloader.enhancer;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class NoEnhancement implements Enhancer {
	
	static class TracingVisitor extends ClassAdapter {

		public TracingVisitor(ClassVisitor classVisitor) {
			super(classVisitor);
		}
		
		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			System.out.println("class: "+name);
			super.visit(version, access, name, signature, superName, interfaces);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			System.out.println("  field: "+name+" "+desc);
			return super.visitField(access, name, desc, signature, value);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			System.out.println("  method: "+name+" "+desc);
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		@Override
		public void visitEnd() {
			super.visitEnd();
		}
	}

	@Override
	public ClassVisitor enhance(ClassVisitor visitor) {
		return visitor;
//		return new TracingVisitor(visitor);
	}

}
