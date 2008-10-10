package sneer.pulp.blinkinglights;

public interface Light {

	boolean isOn();
	
	LightType type();
	String caption();
	Throwable error();
	String helpMessage();
}
