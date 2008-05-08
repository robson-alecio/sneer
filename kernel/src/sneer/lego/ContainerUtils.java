package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container _container; 
	
	public static Container getContainer() {
		return getContainer(null);
	}

	public static Container getContainer(Binder binder) {
		if(_container == null) _container = new SimpleContainer(binder);
		return _container;
	}

    public static Container newContainer(Binder binder) {
        return new SimpleContainer(binder);
    }
	
    public static void stopContainer() {
    	_container = null;
    }
    
    public static void inject(Object component) {
    	getContainer().inject(component);
    }
}
