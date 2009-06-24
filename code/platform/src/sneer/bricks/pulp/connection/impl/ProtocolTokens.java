package sneer.bricks.pulp.connection.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Lang;

interface ProtocolTokens {

	static final byte[] FALLBACK = my(Lang.class).strings().toByteArray("Fallback");
	static final byte[] OK = my(Lang.class).strings().toByteArray("OK");
	static final byte[] SNEER_WIRE_PROTOCOL_1 = my(Lang.class).strings().toByteArray("Sneer Wire Protocol 1");

}
