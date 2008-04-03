package functional.adapters;

import sneer.bricks.network.Network;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;

public class SelfInject {
	
	protected Container _container;
	
	public SelfInject() {
		this(null);
	}
	
	public SelfInject(Network network) {
		if(network == null) {
			_container = ContainerUtils.newContainer(null, null);
		} else {
			_container = ContainerUtils.newContainer(new SimpleBinder().bind(Network.class).toInstance(network), null); 
		}
		selfInject();
	}
	
	public void selfInject() {
		_container.inject(this);
	}
	
	public void inject(Object component) {
		_container.inject(component);
	}
	
	
}
