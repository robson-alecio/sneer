package sneerapps.wind.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.TupleSpace;
import wheel.lang.exceptions.NotImplementedYet;

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
		_space.publish(new Affinity(_keyManager.ownPublicKey(), peerPK, percentage));
	}

	@Override
	public Float affinityFor(PublicKey peer) {
		throw new NotImplementedYet("Use the newest Affinity, not the first found");
		
//		for (Affinity affinity : _space.tuples(Affinity.class))
//			if (affinity.peer.equals(peer))
//				return affinity.percentage;
//		
//		return 0f;
	}

}
