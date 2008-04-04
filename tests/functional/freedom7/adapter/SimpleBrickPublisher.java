package functional.freedom7.adapter;

import java.io.File;

import sneer.bricks.deployer.Deployer;
import sneer.bricks.inspector.BrickInfo;
import sneer.bricks.inspector.BrickInspector;
import sneer.lego.Brick;
import functional.SovereignParty;
import functional.adapters.SelfInject;
import functional.freedom7.BrickPublished;
import functional.freedom7.BrickPublisher;

public class SimpleBrickPublisher implements BrickPublisher {
	
	@Brick
	private Deployer _deployer;
	
	private BrickInspector _inspector;
	
	private SovereignParty _party;
	
	public SimpleBrickPublisher(SovereignParty party) {
		_party = party;
		SelfInject self = (SelfInject) party;
		self.inject(this);
	}
	
	@Override
	public void meToo(String interface1) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public BrickPublished publishBrick(File brickDirectory) {
		_deployer.toString();
		BrickInfo brickInfo = _inspector.loadBrickInfo(brickDirectory);
		String brickName = brickInfo.interfaceName();
		String impl = brickInfo.impl();
		
		System.out.println(brickDirectory);
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

		@Override
	public void bidirectionalConnectTo(SovereignParty peer) {
		_party.bidirectionalConnectTo(peer);
	}

	@Override
	public void giveNicknameTo(SovereignParty peer, String nickname) {
		_party.giveNicknameTo(peer, nickname);
	}

	@Override
	public String navigateAndGetName(String nicknamePath) {
		return _party.navigateAndGetName(nicknamePath);
	}

	@Override
	public String ownName() {
		return _party.ownName();
	}

	@Override
	public void setOwnName(String newName) {
		_party.setOwnName(newName);
	}
}
