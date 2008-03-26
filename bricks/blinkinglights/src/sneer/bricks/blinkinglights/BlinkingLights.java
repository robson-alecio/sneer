package sneer.bricks.blinkinglights;

public interface BlinkingLights {

	void handle(String message, Throwable t);
	
	void handle(Throwable t);
}
