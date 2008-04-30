package sneer.bricks.mesh.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.mesh.Party;
import wheel.reactive.Signal;

abstract class AbstractParty implements Party {

	Map<String, AbstractParty> _remoteProxiesByNickname = new HashMap<String, AbstractParty>(); 
	
	@Override
	public <T> Party navigateTo(String nickname) {
		AbstractParty result = _remoteProxiesByNickname.get(nickname);
		if (result == null) {
			result = produceProxyFor(nickname);
			_remoteProxiesByNickname.put(nickname, result);
		}
		return result;
	}

	void crashProxy(Signal<String> nickname) {
		_remoteProxiesByNickname.remove(nickname).crash();
	}
	
	abstract AbstractParty produceProxyFor(String nickname);

	abstract void crash();

}
