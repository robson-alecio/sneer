package sneerapps.wind.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.TupleSpace;
import wheel.lang.FrozenTime;
import wheel.lang.Predicate;

public class AffinityManagerImpl implements AffinityManager {

	@Inject
	static private KeyManager _keyManager;

	@Inject
	static private TupleSpace _space;

	{
		setAffinityFor(_keyManager.ownPublicKey(), 100f);
	}
	
	@Override
	public void setAffinityFor(PublicKey peerPK, float percentage) {
		_space.publish(new Affinity(_keyManager.ownPublicKey(), FrozenTime.frozenTimeMillis(), peerPK, percentage));
	}

	@Override
	public Float affinityFor(final PublicKey peer) {
		Predicate<Affinity> predicate = new Predicate<Affinity>() { @Override public boolean evaluate(Affinity affinity) {
			return affinity.peer == peer;
		}};
			
		Affinity affinity = _space.mostRecentTuple(Affinity.class, _keyManager.ownPublicKey(), predicate);
		return affinity.percentage;
	}

}
