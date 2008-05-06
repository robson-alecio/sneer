package functional.adapters.freedom7;

import java.io.File;

import sneer.bricks.brickmanager.BrickManager;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.Deployer;
import sneer.lego.Inject;
import wheel.reactive.Signal;
import functional.SovereignParty;
import functional.adapters.SelfInject;
import functional.freedom7.BrickPublisher;

public class SimpleBrickPublisher implements BrickPublisher {
	
	@Inject
	private Deployer _deployer;
	
	@Inject
	private BrickManager _registry;
	
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
	public void publishBrick(File sourceDirectory) {
		BrickBundle brickBundle = _deployer.pack(sourceDirectory);
		//brickBundle.prettyPrint();
		_registry.install(brickBundle);
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
	public Signal<String> navigateAndGetName(String nicknamePath) {
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
