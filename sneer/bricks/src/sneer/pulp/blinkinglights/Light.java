package sneer.pulp.blinkinglights;


public interface Light {

	boolean isOn();
	
	String message();
	
	Throwable error();

	LightType type();
}
