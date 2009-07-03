package sneer.bricks.pulp.blinkinglights.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.blinkinglights.ConfirmationLight;
import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.foundation.lang.Consumer;

public class ConfirmationLightImpl extends LightImpl implements	ConfirmationLight {
	
	private final ListRegister<Light> _lights = my(CollectionSignals.class).newListRegister();
	private final Consumer<Boolean> _responseReceiver;

	public ConfirmationLightImpl(Consumer<Boolean> responseReceiver) {
		super(LightType.CONFIRMATION);
		_responseReceiver = responseReceiver;
	}

	@Override
	public void sayYes(){
		_responseReceiver.consume(true);
		_lights.remove(this);
	}
	
	@Override
	public void sayNo(){
		_responseReceiver.consume(false);
		_lights.remove(this);
	}
}
