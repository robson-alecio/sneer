package spikes.igor;

import javassist.CannotCompileException;

public class ModComEvaluatorFactory {

	private static int count = 0;

	public static ModComConditionEvaluator getEvaluator(String condition) throws CannotCompileException, InstantiationException, IllegalAccessException {
		ClassBlueprint classBlueprint = new ClassBlueprint("Class_" + count);
		classBlueprint.addInterface("objective.sms.objectivesms.services.comunicacao.consulta.ModComConditionEvaluator");
		classBlueprint.addMethod("public boolean evaluate(Object element) { return " + condition + "; }");

		count++;

		return (ModComConditionEvaluator) ClassFactory.getClass(classBlueprint).newInstance();
	}
}