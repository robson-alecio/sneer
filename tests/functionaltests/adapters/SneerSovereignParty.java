package functionaltests.adapters;

import sneer.lego.Brick;
import sneer.lego.ContainerUtils;
import spikes.legobricks.name.NameKeeper;
import functionaltests.SovereignParty;

public class SneerSovereignParty implements SovereignParty {

	@Brick
	private NameKeeper _keeper;
	
	public SneerSovereignParty(String name) {
		ContainerUtils.getContainer().inject(this);
		setOwnName(name);
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
		return _keeper.getName();
	}

	@Override
	public void setOwnName(String newName) {
		_keeper.setName(newName);
	}

}
