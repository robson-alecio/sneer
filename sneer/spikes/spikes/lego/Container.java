package spikes.lego;

public interface Container {

	<T> T produce(Class<T> clazz);

}