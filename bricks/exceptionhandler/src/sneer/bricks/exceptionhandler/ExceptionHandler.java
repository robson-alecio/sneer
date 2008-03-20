package sneer.bricks.exceptionhandler;

public interface ExceptionHandler {

	void handle(String message, Throwable t);
	
	void handle(Throwable t);
}
