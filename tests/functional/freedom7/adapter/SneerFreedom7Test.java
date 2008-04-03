package functional.freedom7.adapter;

import java.io.File;

import functional.SovereignCommunity;
import functional.SovereignParty;
import functional.adapters.SneerCommunity;
import functional.freedom7.BrickPublished;
import functional.freedom7.BrickPublisher;
import functional.freedom7.Freedom7Test;


public class SneerFreedom7Test extends Freedom7Test {

	@Override
	protected BrickPublisher wrapParty(final SovereignParty party) {
		
		return new BrickPublisher() {
			
			@Override
			public void meToo(String interface1) {
				throw new wheel.lang.exceptions.NotImplementedYet();
			}

			@Override
			public BrickPublished publishBrick(File brickFile) {
				throw new wheel.lang.exceptions.NotImplementedYet();
			}

			@Override
			public void bidirectionalConnectTo(SovereignParty peer) {
				party.bidirectionalConnectTo(peer);
			}

			@Override
			public void giveNicknameTo(SovereignParty peer, String nickname) {
				party.giveNicknameTo(peer, nickname);
			}

			@Override
			public String navigateAndGetName(String nicknamePath) {
				return party.navigateAndGetName(nicknamePath);
			}

			@Override
			public String ownName() {
				return party.ownName();
			}

			@Override
			public void setOwnName(String newName) {
				party.setOwnName(newName);
			}};
	}

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}
}
