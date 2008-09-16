package sneer.pulp.blinkinglights;


public interface Light {

	boolean isOn();
	
	void turnOff();
	
	String message();
	
	Throwable error();

	LightType type();
}
