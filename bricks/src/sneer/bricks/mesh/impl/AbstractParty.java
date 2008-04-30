package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.mesh.Party;

abstract class AbstractParty implements Party {

	Map<String, AbstractParty> _remoteProxiesByNickname = new HashMap<String, AbstractParty>(); 
	
	@Override
	public Party navigateTo(String nickname) {
		AbstractParty result = _remoteProxiesByNickname.get(nickname);
		if (result == null) {
			result = produceProxyFor(nickname);
			if (result == null) return null;
			
			_remoteProxiesByNickname.put(nickname, result);
		}
		return result;
	}

	abstract AbstractParty produceProxyFor(String nickname);

}
