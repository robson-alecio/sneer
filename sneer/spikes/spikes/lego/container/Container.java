package spikes.lego.container;

public interface Container {

	<T> T produce(Class<T> clazz);

	<T> T create(Class<T> clazz);

}