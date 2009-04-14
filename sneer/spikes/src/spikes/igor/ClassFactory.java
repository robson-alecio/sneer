package spikes.igor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class ClassFactory {

	private static ClassPool classPool = ClassPool.getDefault(); 

	private static void setClassSuperclass(CtClass ctClass, ClassBlueprint classBlueprint) throws CannotCompileException {
		String superClassName = classBlueprint.getSuperclass();

		if(superClassName != null) {
			CtClass ctSuperClass;
			try {
				ctSuperClass = classPool.get(superClassName);
			} catch (NotFoundException nfe) {
				ctSuperClass = classPool.makeClass(superClassName);
			}
			ctClass.setSuperclass(ctSuperClass);
		}
	}

	private static void setClassInterfaces(CtClass ctClass, ClassBlueprint classBlueprint) {
		String[] interfaces = classBlueprint.getInterfaces();
		CtClass ctInterface;
		for (int i = 0; i < interfaces.length; i++) {
			try {
				ctInterface = classPool.get(interfaces[i]);
			} catch (NotFoundException nfe) {
				ctInterface = classPool.makeInterface(interfaces[i], ctClass);
			}
		ctClass.addInterface(ctInterface);
		}
	}

	private static void setClassAttributes(CtClass ctClass, ClassBlueprint classBlueprint) throws CannotCompileException {
		String[] attributes = classBlueprint.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			CtField ctField = CtField.make(attributes[i], ctClass);
			ctClass.addField(ctField);
		}
	}

	private static void setClassMethods(CtClass ctClass, ClassBlueprint classBlueprint) throws CannotCompileException {
		String[] methodsDefinitions = classBlueprint.getMethods();
		for (int i = 0; i < methodsDefinitions.length; i++) {
			CtMethod ctMethod = CtNewMethod.make(methodsDefinitions[i], ctClass);
			ctClass.addMethod(ctMethod);
		}
	}

	public static <T> Class<T> getClass(ClassBlueprint classBlueprint) throws CannotCompileException {
		CtClass ctClass = classPool.makeClass(classBlueprint.getName());

		// Set superclass
		setClassSuperclass(ctClass, classBlueprint);

		// Set interfaces
		setClassInterfaces(ctClass, classBlueprint);

		// Set attributes
		setClassAttributes(ctClass, classBlueprint);

		// Set methods
		setClassMethods(ctClass, classBlueprint);

		return ctClass.toClass();
	}

}