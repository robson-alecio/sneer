package sneerapps.wind.impl;

import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.serialization.mocks.XStreamBinarySerializer;
import sneer.lego.Inject;
import sneerapps.wind.Environment;
import sneerapps.wind.Probe;
import sneerapps.wind.ProbeFactory;
import wheel.io.serialization.DeepCopier;
import wheel.lang.Omnivore;

public class ProbeFactoryImpl implements ProbeFactory, Omnivore<Object> {

	private static final XStreamBinarySerializer SERIALIZER = new XStreamBinarySerializer();
	
	@Inject
	static private Environment _environment;

	@Override
	public Probe produceProbeFor(PublicKey peerPK) {
		return new ProbeImpl(this);
	}

	@Override
	public void consume(Object tuple) {
		_environment.publish(DeepCopier.deepCopy(tuple, SERIALIZER));
	}

}
