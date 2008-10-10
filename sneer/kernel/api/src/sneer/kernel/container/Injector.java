package sneer.kernel.container;



public interface Injector extends Brick
{
    void inject(Object obj) 
        throws ContainerException;

	void inject(Class<?> clazz)
		throws ContainerException;
}