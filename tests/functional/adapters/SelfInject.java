package functional.adapters;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.network.Network;
import sneer.lego.Binder;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;

public class SelfInject {
	
	protected Container _container;
	
	public SelfInject() {
		this(null, null);
	}
	
	public SelfInject(Network network, SneerConfig config) {
		Binder binder = new SimpleBinder();
		
		if(network != null) 
			binder.bind(Network.class).toInstance(network);

		if(config != null) 
			binder.bind(SneerConfig.class).toInstance(config);
	
		_container = ContainerUtils.newContainer(binder);
		_container.inject(this);
	}
	
	public void inject(Object component) {
		_container.inject(component);
	}

}
