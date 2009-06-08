package spikes.igor;

import javassist.CannotCompileException;

public class Test {

	public static void main(String[] args) throws CannotCompileException,InstantiationException, IllegalAccessException {
		ModComConditionEvaluator evaluator1 = ModComEvaluatorFactory.getEvaluator("element == null");
		ModComConditionEvaluator evaluator2 = ModComEvaluatorFactory.getEvaluator("element != null");

		System.out.print("Evaluator1: Codition \"element == null\", evaluated object \"null\" --> ");
		System.out.println(evaluator1.evaluate(null));

		System.out.print("Evaluator1: Codition \"element == null\", evaluated object \"new Object()\" --> ");
		System.out.println(evaluator1.evaluate(new Object()));

		System.out.println("**********************************************************************************");

		System.out.print("Evaluator2: Codition \"element != null\", evaluated object \"null\" --> ");
		System.out.println(evaluator2.evaluate(null));

		System.out.print("Evaluator2: Codition \"element != null\", evaluated object \"new Object()\" --> ");
		System.out.println(evaluator2.evaluate(new Object()));

		}
	}