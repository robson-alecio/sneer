package sneer.bricks.hardware.io.log.formatter.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.io.log.formatter.LogFormatter;
import sneer.foundation.brickness.testsupport.BrickTest;


public class LogFormatterTest extends BrickTest {

	@SuppressWarnings("unused")	private Object _referenceToAvoidGc;

	@Test
	public void insets() {
		assertFormatting(
			"User Peter is not allowed to access the TPS report.",
			"User {} is not allowed to access the {} report.", "Peter", "TPS"
		);
	}

	@Test
	public void omittingLastBrackets() {
		assertFormatting(
			"User Peter is not allowed to access report: TPS",
			"User {} is not allowed to access report: ", "Peter", "TPS"
		);
	}

	@Test
	public void noBrackets() {
		assertFormatting(
			"User is not allowed to access report: TPS",
			"User is not allowed to access report: ", "TPS"
		);
	}

	@Test
	public void noInsets() {
		assertFormatting(
			"Hello world",
			"Hello world"
		);
	}

	private void assertFormatting(String expected, String message, Object... insets) {
		String formatted = my(LogFormatter.class).format(message, insets);
		assertTrue(formatted.indexOf(expected) != -1);
	}
}
