package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container _container; 
	
	public static Container getContainer() {
		if(_container == null) _container = new SimpleContainer();
		return _container;
	}

    public static Container newContainer(Binder binder) {
        return new SimpleContainer(binder);
    }

    public static Container newContainer() {
        return new SimpleContainer();
    }
	
    public static void stopContainer() {
    	_container = null;
    }
    
    public static void inject(Object component) {
    	getContainer().inject(component);
    }
}
