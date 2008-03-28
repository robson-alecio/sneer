package sneer.lego;

public interface Container extends Crashable {

    /**
     * Creates a new instance of clazz after checking the cache 
     */
	<T> T produce(Class<T> clazz) throws LegoException;

	<T> T produce(String className) throws LegoException;

	<T> T create(Class<T> clazz) throws LegoException;

	void inject(Object component);
}