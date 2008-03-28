package sneer.bricks.blinkinglights;

public interface Light {

	boolean isOn();
	
	void turnOff();
	
	String message();
	
	Throwable error();
	
	long timeout();
	
	void renew();
}
