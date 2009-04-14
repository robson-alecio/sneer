package spikes.igor;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class ModComEvaluatorFactory {

	private static int count = 0;

	public static ModComConditionEvaluator getEvaluator(String condition) throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException {
		ClassBlueprint classBlueprint = new ClassBlueprint("Class_" + count);
//		classBlueprint.setSuperclass("brundle.foundation.abstractsuperclasses.Core");
		classBlueprint.addInterface("objective.sms.objectivesms.services.comunicacao.consulta.ModComConditionEvaluator");
		classBlueprint.addMethod("public boolean evaluate(Object element) { return " + condition + "; }");

		count++;

		return (ModComConditionEvaluator) ClassFactory.getClass(classBlueprint).newInstance();
	}
}