package sneer.lego.impl;

import sneer.lego.LegoException;

public interface Injector
{
    void inject(Object obj) 
        throws LegoException;
}