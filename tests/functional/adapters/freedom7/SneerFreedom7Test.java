package functional.adapters.freedom7;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

import functional.SovereignCommunity;
import functional.SovereignParty;
import functional.adapters.SneerCommunity;
import functional.freedom7.BrickPublisher;
import functional.freedom7.Freedom7Test;


public class SneerFreedom7Test extends Freedom7Test {

	@Override
	protected BrickPublisher wrapParty(final SovereignParty party) {
		return new SimpleBrickPublisher(party);
	}

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}

	@Override
	protected File askSourceFolder() {
		return new File(SystemUtils.getUserDir(), "/tests/functional/freedom7/test-resources/bricks/source");
	}
}