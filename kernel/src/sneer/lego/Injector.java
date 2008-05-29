package sneer.lego;


public interface Injector extends Brick
{
    void inject(Object obj) 
        throws LegoException;

	void inject(Class<?> clazz)
		throws LegoException;
}