package sneer.bricks.network.computers.sockets.protocol;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.foundation.brickness.Brick;

@Brick
public interface ProtocolTokens {

	static final byte[] FALLBACK = my(Lang.class).strings().toByteArray("Fallback");
	static final byte[] CONFIRMED = my(Lang.class).strings().toByteArray("Confirmed");
	static final byte[] SNEER_WIRE_PROTOCOL_1 = my(Lang.class).strings().toByteArray("Sneer Wire Protocol 1");

}
