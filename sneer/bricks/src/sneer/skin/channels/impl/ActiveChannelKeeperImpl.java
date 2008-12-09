package sneer.skin.channels.impl;

import sneer.skin.channels.ActiveChannelKeeper;
import wheel.lang.Consumer;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ActiveChannelKeeperImpl implements ActiveChannelKeeper {

	private final Register<Integer> _register = new RegisterImpl<Integer>(0);

	@Override
	public Signal<Integer> channel() {
		return _register.output();
	}

	@Override
	public Consumer<Integer> channelSetter() {
		return _register.setter();
	}

}
