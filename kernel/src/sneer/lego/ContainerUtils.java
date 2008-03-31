package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container _container; 
	
	public static Container getContainer() {
		return getContainer(null, null);
	}

	public static Container getContainer(Binder binder, ConfigurationFactory configurationFactory) {
		if(_container == null) _container = new SimpleContainer(binder, configurationFactory);
		return _container;
	}

    public static Container newContainer(Binder binder, ConfigurationFactory configurationFactory) {
        return new SimpleContainer(binder, configurationFactory);
    }
	
    public static void stopContainer() {
    	_container = null;
    }
    
    public static void inject(Object component) {
    	getContainer().inject(component);
    }
}
