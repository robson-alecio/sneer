package sneer.pulp.natures.gui.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import sneer.brickness.ClassDefinition;
import sneer.commons.environments.Environments;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.natures.gui.GUI;

class GUIImpl implements GUI {
	
	private final ClassPool classPool = new ClassPool(); {
		classPool.appendSystemPath();
	}

	@Override
	public List<ClassDefinition> realize(final ClassDefinition classDef) {
		
		final ArrayList<ClassDefinition> result = new ArrayList<ClassDefinition>();
		try {
			final CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classDef.bytes()));
			for (CtMethod m : ctClass.getDeclaredMethods()) {
				String thunkName = "_" + m.getName();
				result.add(makeThunkFor(thunkName, m));
				enhanceMethod(thunkName, m);
			}
			result.add(toClassDefinition(ctClass));
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (CannotCompileException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (NotFoundException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} 
		return result;
	}

	private ClassDefinition toClassDefinition(final CtClass ctClass)
			throws IOException, CannotCompileException {
		return new ClassDefinition(ctClass.getName(), ctClass.toBytecode());
	}

	private void enhanceMethod(String thunkName, CtMethod m) throws CannotCompileException {
		
		final String thunkFullName = m.getDeclaringClass().getName() + "." + thunkName;
		final String guiThreadClass = GuiThread.class.getName();
		m.insertBefore("if (!java.awt.EventQueue.isDispatchThread()) {"
					+ thunkFullName + " thunk = new " + thunkFullName + "(this);"
					+ "((" + guiThreadClass + ")" + Environments.class.getName() + ".my(" + guiThreadClass + ".class)).strictInvokeAndWait(thunk);"
					+ "return thunk.result;"
					+ "}");
	}

	private ClassDefinition makeThunkFor(String thunkName, CtMethod m) throws NotFoundException,
			CannotCompileException, IOException {
		final String targetType = m.getDeclaringClass().getName();
		
		final CtClass thunkClass = m.getDeclaringClass().makeNestedClass(thunkName, true);
		thunkClass.addInterface(ctClassFor(Runnable.class));
		thunkClass.addField(CtField.make("public " + m.getReturnType().getName() + " result;", thunkClass));
		thunkClass.addField(CtField.make("private final " + targetType + " target;", thunkClass));
		thunkClass.addConstructor(
				CtNewConstructor.make(
					"public " + thunkName + "(" + targetType + " target) {" +
						"this.target = target;" +
					"}", thunkClass));
		thunkClass.addMethod(
				CtNewMethod.make(
					"public void run() {" +
						"result = target." + m.getName() + "();" +
					"}", thunkClass));
		
		return toClassDefinition(thunkClass);
	}

	private CtClass ctClassFor(final Class<?> clazz)
			throws NotFoundException {
		return classPool.get(clazz.getName());
	}

	

}
