package sneer.kernel.container;



public interface Container {
	
	<T> T produce(Class<T> type) throws LegoException;

	Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException;

}