package functional.adapters;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.network.Network;
import sneer.lego.Binder;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;

public class SelfInject {
	
	private final Container _container;
	
	public SelfInject(Network network, SneerConfig config) {
		Binder binder = new SimpleBinder();
		binder.bind(Network.class).toInstance(network);
		binder.bind(SneerConfig.class).toInstance(config);
	
		_container = ContainerUtils.newContainer(binder);
		_container.inject(this);
	}
	
	public void inject(Object component) {
		_container.inject(component);
	}

}
