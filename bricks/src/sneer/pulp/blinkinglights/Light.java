package sneer.pulp.blinkinglights;

public interface Light {

	public static int INFO_TYPE = 0;
	public static int WARN_TYPE = 1;
	public static int ERROR_TYPE = 2;
	
	boolean isOn();
	
	void turnOff();
	
	String message();
	
	Throwable error();

	boolean isInfo();
	boolean isWarn();
	boolean isError();
}
