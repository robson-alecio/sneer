/**
 * 
 */
package sneer.pulp.natures.gui.impl;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import sneer.brickness.ClassDefinition;

/**
 * Prefixes every method with the following code:
 * 
 * if (!EventQueue.isDispatchThread()) {
 * 	$innerClassType thunk = new $innerClassType();
 * 	wheel.io.ui.GuiThread.strictInvokeAndWait(thunk);
 *  return thunk.result;
 * }
 **/
final class GUIMethodEnhancer extends AdviceAdapter {
	
	private final String _desc;
	private final String _methodName;
	private final ClassDefinition _outerClassDef;
	private final ClassDefinition _innerClassDef;

	GUIMethodEnhancer(MethodVisitor methodVisitor,
			int access,
			String methodName,
			String desc,
			ClassDefinition mainClassDef,
			ArrayList<ClassDefinition> result) {
		super(methodVisitor, access, methodName, desc);
		_desc = desc;
		_methodName = methodName;
		_outerClassDef = mainClassDef;
		_innerClassDef = newMethodThunkFor();
		result.add(_innerClassDef);
	}

	@Override
	protected void onMethodEnter() {
		
		// EventQueue.isDispatchThread
		mv.visitMethodInsn(INVOKESTATIC, "java/awt/EventQueue", "isDispatchThread", "()Z");
		
		Label elseLabel = new Label();
		
		// if (!...) {
		mv.visitJumpInsn(IFNE, elseLabel);

		// new Thunk() {}
		mv.visitTypeInsn(NEW, innerClassDescriptor());
		mv.visitInsn(DUP);
		
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, innerClassDescriptor(), "<init>", "(L" + outerClassDescriptor() + ";)V");
		
		final int thunkVariable = newLocal(Type.getType(outerClassType()));
		mv.visitVarInsn(ASTORE, thunkVariable);
		mv.visitVarInsn(ALOAD, thunkVariable);
		mv.visitMethodInsn(INVOKESTATIC, "wheel/io/ui/GuiThread", "strictInvokeAndWait", "(Ljava/lang/Runnable;)V");
		if (hasReturnValue()) {
			mv.visitVarInsn(ALOAD, thunkVariable);
			mv.visitFieldInsn(GETFIELD, innerClassDescriptor(), "result", returnType());
			mv.visitInsn(ARETURN);
		} else {
			mv.visitInsn(RETURN);
		}
		
		// }
		mv.visitLabel(elseLabel);
	}

	private String returnType() {
		return StringUtils.substringAfter(_desc, ")");
	}

	private boolean hasReturnValue() {
		return !returnType().equals("V");
	}

	protected ClassDefinition newMethodThunkFor() {
		
		/*
		 * class $innerClassDescriptor implements Runnable {
		 * 	public $returnType result;
		 * 
		 *  public void run() {
		 *  	result = $methodName();
		 *  }
		 * 
		 * }
		 */
		
		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_6, ACC_SUPER, innerClassDescriptor(), null, "java/lang/Object", new String[] { "java/lang/Runnable" });
		cw.visitInnerClass(innerClassDescriptor(), outerClassDescriptor(), _methodName, ACC_PRIVATE);

		emitOuterThisField(cw);
		emitResultField(cw);
		emitConstructor(cw);
		emitRunMethod(cw);
		
		cw.visitEnd();

		return new ClassDefinition(innerClassName(), cw.toByteArray()); 
	}

	private String innerClassName() {
		return innerClassDescriptor().replace('/', '.');
	}

	private void emitRunMethod(ClassWriter cw) {
		MethodVisitor run = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		run.visitCode();
		if (hasReturnValue())
			run.visitVarInsn(ALOAD, 0);
		
		run.visitVarInsn(ALOAD, 0);
		run.visitFieldInsn(GETFIELD, innerClassDescriptor(), "this$0", outerClassType());
		run.visitMethodInsn(INVOKEVIRTUAL, outerClassDescriptor(), _methodName, methodDesc);
		
		if (hasReturnValue())
			run.visitFieldInsn(PUTFIELD, innerClassDescriptor(), "result", returnType());
		
		run.visitInsn(RETURN);
		run.visitMaxs(2, 1);
		run.visitEnd();
	}

	private String innerClassDescriptor() {
		return outerClassDescriptor() + "$" + _methodName;
	}

	private String outerClassType() {
		return "L" + outerClassDescriptor() + ";";
	}

	private String outerClassDescriptor() {
		return _outerClassDef.name().replace('.', '/');
	}

	private void emitOuterThisField(ClassWriter cw) {
		FieldVisitor outerThis = cw.visitField(ACC_FINAL + ACC_SYNTHETIC, "this$0", outerClassType(), null, null);
		outerThis.visitEnd();
	}

	private void emitResultField(ClassWriter cw) {
		FieldVisitor resultField = cw.visitField(ACC_PUBLIC, "result", returnType(), null, null);
		resultField.visitEnd();
	}

	private void emitConstructor(ClassWriter cw) {
		MethodVisitor constructor = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + outerClassType() + ")V", null, null);
		constructor.visitCode();
		constructor.visitVarInsn(ALOAD, 0);
		constructor.visitVarInsn(ALOAD, 1);
		constructor.visitFieldInsn(PUTFIELD, innerClassDescriptor(), "this$0", outerClassType());
		constructor.visitVarInsn(ALOAD, 0);
		constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		constructor.visitInsn(RETURN);
		constructor.visitMaxs(2, 2);
		constructor.visitEnd();
	}
}