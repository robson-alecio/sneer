package sneer.bricks.pulp.natures.gui.impl;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import javassist.*;
import sneer.bricks.pulp.natures.gui.*;
import sneer.foundation.brickness.*;
import sneer.foundation.environments.*;

class GUIImpl implements GUI {
	
	private final ClassPool classPool = new ClassPool(true);

	@Override
	public List<ClassDefinition> realize(final ClassDefinition classDef) {
		
		final ArrayList<ClassDefinition> result = new ArrayList<ClassDefinition>();
		try {
			final CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classDef.bytes));
			CtClass metadata = null;
			try {
				metadata = defineBrickMetadata(ctClass);
				if (isBrickImplementation(ctClass)) {
					result.add(toClassDefinition(metadata));
					introduceMetadataInitializer(ctClass);
				}
				enhanceMethods(ctClass, result);
				result.add(toClassDefinition(ctClass));
			} finally {
				ctClass.detach();
				if(metadata != null)
					metadata.detach();
			}
			return result;
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (CannotCompileException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (NotFoundException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void introduceMetadataInitializer(CtClass ctClass) {
		try {
			ctClass.makeClassInitializer().insertAfter("natures.gui.BrickMetadata.ENVIRONMENT = sneer.foundation.environments.Environments.my(sneer.foundation.environments.Environment.class);");
		} catch (CannotCompileException e) {
			throw new IllegalStateException(e);
		}
	}

	private CtClass defineBrickMetadata(@SuppressWarnings("unused") CtClass brickClass) {
		CtClass metadata = classPool.makeClass("natures.gui.BrickMetadata");
		metadata.setModifiers(javassist.Modifier.PUBLIC);
		try {
			metadata.addField(CtField.make("public static " + Environment.class.getName() + " ENVIRONMENT;", metadata));
			return metadata;
		} catch (CannotCompileException e) {
			throw new IllegalStateException(e);
		}
	}

	private boolean isBrickImplementation(CtClass ctClass) throws NotFoundException {
		for (CtClass intrface : ctClass.getInterfaces())
			if (isBrickInterface(intrface))
				return true;
		return false;
	}

	private boolean isBrickInterface(CtClass intrface) {
		for (Object annotation : intrface.getAvailableAnnotations())
			if (annotation instanceof Brick)
				return true;
		return false;
	}

	private void enhanceMethods(final CtClass ctClass,
			final ArrayList<ClassDefinition> result) {
		for (CtMethod m : ctClass.getDeclaredMethods()) {
			if (Modifier.isStatic(m.getModifiers()))
				continue;
			new GUIMethodEnhancer(classPool, ctClass, m, result).run();
		}
	}

	public static ClassDefinition toClassDefinition(final CtClass ctClass)
			throws CannotCompileException {
		try {
			return new ClassDefinition(ctClass.getName(), ctClass.toBytecode());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	

}
