package sneer.lego;


public interface Injector
{
    void inject(Object obj) 
        throws LegoException;
}