package sneer.bricks.network.computers.sockets.protocol;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.lang.StringUtils;
import sneer.foundation.brickness.Brick;

@Brick
public interface ProtocolTokens {

	byte[] FALLBACK = my(StringUtils.class).toByteArray("Fallback");
	byte[] OK = my(StringUtils.class).toByteArray("OK");
	byte[] SNEER_WIRE_PROTOCOL_1 = my(StringUtils.class).toByteArray("Sneer Wire Protocol 1");

}
