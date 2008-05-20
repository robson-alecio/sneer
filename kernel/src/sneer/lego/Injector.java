package sneer.lego;


public interface Injector
{
    void inject(Object obj) 
        throws LegoException;

	void inject(Class<?> clazz)
		throws LegoException;
}