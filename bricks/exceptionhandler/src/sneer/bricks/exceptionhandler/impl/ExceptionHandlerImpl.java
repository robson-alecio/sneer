package sneer.bricks.exceptionhandler.impl;

import sneer.bricks.exceptionhandler.ExceptionHandler;

public class ExceptionHandlerImpl implements ExceptionHandler {

	@Override
	public void handle(String message, Throwable t) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void handle(Throwable t) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
