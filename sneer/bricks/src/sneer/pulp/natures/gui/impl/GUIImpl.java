package sneer.pulp.natures.gui.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sneer.brickness.ClassDefinition;
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
				if (Modifier.isStatic(m.getModifiers()))
					continue;
				new GUIMethodEnhancer(classPool, ctClass, m, result).run();
			}
			result.add(toClassDefinition(ctClass));
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (CannotCompileException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} 
		return result;
	}

	public static ClassDefinition toClassDefinition(final CtClass ctClass)
			throws IOException, CannotCompileException {
		return new ClassDefinition(ctClass.getName(), ctClass.toBytecode());
	}

	

}
