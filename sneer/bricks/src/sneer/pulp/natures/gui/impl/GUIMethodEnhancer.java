package sneer.pulp.natures.gui.impl;

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

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringUtils;

import sneer.brickness.ClassDefinition;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Pair;
import sneer.hardware.gui.guithread.GuiThread;

public class GUIMethodEnhancer {

	private final String _thunkName;
	private final List<ClassDefinition> _resultingClasses;
	private final CtMethod _method;
	private final ClassPool _classPool;
	private final CtClass _containingClass;

	public GUIMethodEnhancer(ClassPool classPool, CtClass containingClass, CtMethod method, List<ClassDefinition> resultingClasses) {
		_classPool = classPool;
		_containingClass = containingClass;
		_resultingClasses = resultingClasses;
		_method = method;
		_thunkName = "_" + method.getName();
	}

	public void run() {
		try {
			_resultingClasses.add(makeThunkFor());
			enhanceMethod();
		} catch (Exception e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(_method.toString(), e);
		}
	}
	
	private void enhanceMethod() throws CannotCompileException, NotFoundException {
		
		final String thunkFullName = _method.getDeclaringClass().getName() + "." + _thunkName;
		final String guiThreadClass = GuiThread.class.getName();
		_method.insertBefore(
				"if (!java.awt.EventQueue.isDispatchThread()) {"
					+ thunkFullName + " thunk = new " + thunkFullName + "(" + thunkConstructorArguments() + ");"
					+ "((" + guiThreadClass + ")" + Environments.class.getName() + ".my(" + guiThreadClass + ".class)).strictInvokeAndWait(thunk);"
					+ (hasReturnValue() ? "return thunk.result;" : "return;")
				+ "}");
	}

	private String thunkConstructorArguments() throws NotFoundException {
		StringBuilder parameterList = new StringBuilder("this");
		for (int i=0; i<_method.getParameterTypes().length; ++i) {
			parameterList.append(", ");
			parameterList.append("$" + (i + 1));
		}
		return parameterList.toString();
	}

	private boolean hasReturnValue() {
		return !_method.getSignature().endsWith(")V");
	}

	private ClassDefinition makeThunkFor() throws NotFoundException,
			CannotCompileException, IOException {
		final CtClass thunkClass = _containingClass.makeNestedClass(_thunkName, true);
		thunkClass.addInterface(ctClassFor(Runnable.class));
		
		final ArrayList<Pair<String, String>> thunkFields = buildThunkFieldList();
		defineThunkFields(thunkClass, thunkFields);
		
		final String ctorCode = "public " + _thunkName + "(" + thunkParameterList(thunkFields) + ") {\n" +
			thunkFieldAssignments(thunkFields) + 
		"}";
		thunkClass.addConstructor(
				CtNewConstructor.make(
					ctorCode, thunkClass));
		
		final String invocation = "target." + _method.getName() + "(" + targetInvocationList(thunkFields) + ");";
		thunkClass.addMethod(
				CtNewMethod.make(
					"public void run() {" +
						(hasReturnValue() ? "result = " + invocation : invocation)+
					"}", thunkClass));
		
		return new ClassDefinition(thunkClass.getName(), thunkClass.toBytecode());
	}

	private void defineThunkFields(final CtClass thunkClass,
			final ArrayList<Pair<String, String>> thunkFields)
			throws CannotCompileException, NotFoundException {
		for (Pair<String, String> thunkField : thunkFields)
			thunkClass.addField(CtField.make("private final " + thunkField._a + " " + thunkField._b + ";", thunkClass));
		
		if (hasReturnValue())
			thunkClass.addField(CtField.make("public " + _method.getReturnType().getName() + " result;", thunkClass));
	}

	private ArrayList<Pair<String, String>> buildThunkFieldList()
			throws NotFoundException {
		ArrayList<Pair<String, String>> thunkFields = new ArrayList<Pair<String, String>>();
		thunkFields.add(Pair.of(_method.getDeclaringClass().getName(), "target"));
		final CtClass[] parameters = _method.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			thunkFields.add(Pair.of(parameters[i].getName(), "arg" + i));
		}
		return thunkFields;
	}

	private String targetInvocationList(ArrayList<Pair<String, String>> thunkFields) {
		return StringUtils.join(
				CollectionUtils.collect(
					thunkFields.subList(1, thunkFields.size()),
					Pair.<String, String>second()),
				", ");
	}

	private String thunkFieldAssignments(
			ArrayList<Pair<String, String>> thunkFields) {
		return StringUtils.join(
				CollectionUtils.collect(
					thunkFields, new Transformer<Pair<String, String>, String>() { @Override public String transform(Pair<String, String> input) {
						return "this." + input._b + " = " + input._b + ";";
					}}), "\n");
	}

	private String thunkParameterList(ArrayList<Pair<String, String>> thunkFields) {
		return StringUtils.join(
				CollectionUtils.collect(
					thunkFields, new Transformer<Pair<String, String>, String>() { @Override public String transform(Pair<String, String> input) {
						return input._a + " " + input._b;
					}}), ", ");
	}

	private CtClass ctClassFor(final Class<?> clazz)
			throws NotFoundException {
		return _classPool.get(clazz.getName());
	}

	

}
