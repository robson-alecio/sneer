package wheel.io.tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import wheel.io.Logger;

public class LoggerTest {

	@Test
	public void insets() {
		assertLog(
			"User Peter is not allowed to access the TPS report.",
			"User {} is not allowed to access the {} report.", "Peter", "TPS"
		);
	}

	@Test
	public void omittingLastBrackets() {
		assertLog(
			"User Peter is not allowed to access report: TPS",
			"User {} is not allowed to access report: ", "Peter", "TPS"
		);
	}

	@Test
	public void noBrackets() {
		assertLog(
			"User is not allowed to access report: TPS",
			"User is not allowed to access report: ", "TPS"
		);
	}
	
	@Test
	public void noInsets() {
		assertLog(
			"Hello world",
			"Hello world"
		);
	}
	
	
	private void assertLog(String expected, String message, Object... insets) {
		ByteArrayOutputStream observed = new ByteArrayOutputStream();
		Logger.redirectTo(observed);
		Logger.log(message, insets);
		
		assertTrue(observed.toString().indexOf(expected) != -1);
	}


}
