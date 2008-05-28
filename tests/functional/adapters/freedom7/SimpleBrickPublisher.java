package functional.adapters.freedom7;

import java.io.File;

import sneer.bricks.brickmanager.BrickManager;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.BrickFile;
import sneer.bricks.deployer.Deployer;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Container;
import sneer.lego.Inject;
import wheel.reactive.Signal;
import functional.SovereignParty;
import functional.adapters.SelfInject;
import functional.freedom7.BrickPublisher;

public class SimpleBrickPublisher implements BrickPublisher {
	
	@Inject
	private Container _container;
	
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
	public void meToo(BrickPublisher party, String brickName) {
		BrickFile brick = party.brick(brickName);
		_registry.install(brick);
	}

	@Override
	public BrickFile brick(String brickName) {
		return _registry.brick(brickName);
	}

	@Override
	public BrickBundle publishBrick(File sourceDirectory) {
		BrickBundle brickBundle = _deployer.pack(sourceDirectory);
		//brickBundle.prettyPrint();
		_registry.install(brickBundle);
		return brickBundle;
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

	@Override
	public String toString() {
		return ownName();
	}

	@Override
	public Object produce(String brickName) {
		return _container.produce(brickName);
	}

	@Override
	public PublicKey ownPublicKey() {
		return _party.ownPublicKey();
	}
}
