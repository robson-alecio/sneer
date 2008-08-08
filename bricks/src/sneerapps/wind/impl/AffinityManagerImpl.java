package sneerapps.wind.impl;

import static wheel.lang.Nulls.nvl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.Affinity;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.TupleSpace;
import wheel.lang.FrozenTime;
import wheel.lang.Functor;

public class AffinityManagerImpl implements AffinityManager {

	@Inject
	static private KeyManager _keyManager;

	@Inject
	static private TupleSpace _space;

	@Override
	public void setAffinityFor(PublicKey peerPK, float percentage) {
		_space.publish(new Affinity(_keyManager.ownPublicKey(), FrozenTime.frozenTimeMillis(), peerPK, percentage));
	}

	@Override
	public float affinityFor(final PublicKey peer) {
		Map<PublicKey, Float> maxAffinitySoFarByPK = new HashMap<PublicKey, Float>();
		PublicKey ownPublicKey = _keyManager.ownPublicKey();
		maxAffinitySoFarByPK.put(ownPublicKey, 100f);
		
		Set<PublicKey> promisingPaths = new HashSet<PublicKey>();
		promisingPaths.add(ownPublicKey);

		while (!promisingPaths.isEmpty())
			followPromisingPaths(promisingPaths, maxAffinitySoFarByPK);

		return nvl(maxAffinitySoFarByPK.get(peer), 0f);
	}


	private void followPromisingPaths(Set<PublicKey> promisingPaths, Map<PublicKey, Float> maxAffinitySoFarByPK) {
		Iterable<PublicKey> thisRound = new ArrayList<PublicKey>(promisingPaths);
		promisingPaths.clear();
		
		for (PublicKey intermediary : thisRound)
			for (Affinity affinity : affinitiesPublishedBy(intermediary))
				followAffinity(affinity, promisingPaths, maxAffinitySoFarByPK);
	}

	private void followAffinity(Affinity affinity, Set<PublicKey> promisingPaths, Map<PublicKey, Float> maxAffinitySoFarByPK) {
		float newPercentage = maxAffinitySoFarByPK.get(affinity.publisher)
			* affinity.percentage / 100;
		
		float affinitySoFar = nvl(maxAffinitySoFarByPK.get(	affinity.peer), 0f);
		if (newPercentage <= affinitySoFar)
			return;
		
		maxAffinitySoFarByPK.put(affinity.peer, newPercentage);
		promisingPaths.add(affinity.peer);
	}

	
	private Iterable<Affinity> affinitiesPublishedBy(final PublicKey intermediary) {
		Functor<Affinity, Object> grouping = new Functor<Affinity, Object>() { @Override public Object evaluate(Affinity affinity) {
			return affinity.peer; 
		}};
		
		return _space.mostRecentTupleByGroup(Affinity.class, intermediary, grouping);
	}


}
