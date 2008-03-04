package functionaltests.impl.mock;

import functionaltests.SovereignParty;

public class SovereignPartyMock implements SovereignParty {
	
	private String _name;
	
	public SovereignPartyMock(String name) {
		_name = name;
		_name.toString();
	}

	@Override
	public void connectTo(SovereignParty peer) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void giveNicknameTo(SovereignParty peer, String nickname) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public SovereignParty navigateTo(String... nicknamePath) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String ownName() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void setOwnName(String newName) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
