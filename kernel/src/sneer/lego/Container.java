package sneer.lego;

public interface Container {

    /**
     * Creates a new instance of clazz after checking the cache 
     */
	<T> T produce(Class<T> clazz) throws LegoException;

	<T> T produce(String className) throws LegoException;

}