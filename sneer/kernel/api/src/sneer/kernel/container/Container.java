package sneer.kernel.container;



public interface Container {
	
	<T> T produce(Class<T> type) throws ContainerException;

	Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException;

}