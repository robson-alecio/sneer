package sneer.lego;


public interface Container {

	<T> T produce(Class<T> type) throws LegoException;
	<T> T produce(String typeName) throws LegoException;

	<T> T create(Class<T> type) throws LegoException;
	<T> T create(Class<T> type, Object... args) throws LegoException;

}