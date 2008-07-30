package sneerapps.wind.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;

public class AffinityManagerImpl implements AffinityManager {

	@Inject
	static private KeyManager _keyManager;

	private final Map<PublicKey, Float> _affinitiesByPK = new HashMap<PublicKey, Float>();

	{
		setAffinityFor(_keyManager.ownPublicKey(), 100f);
	}
	
	@Override
	public void setAffinityFor(PublicKey publicKey, float percentage) {
		_affinitiesByPK.put(publicKey, percentage);
	}

	@Override
	public Float affinityFor(PublicKey peer) {
		Float result = _affinitiesByPK.get(peer);
		
		return result == null
			? 0f
			: result;
	}

}
