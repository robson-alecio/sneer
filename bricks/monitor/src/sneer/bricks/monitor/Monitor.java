package sneer.bricks.monitor;

public interface Monitor {

	void handle(String message, Throwable t);
	
	void handle(Throwable t);
}
