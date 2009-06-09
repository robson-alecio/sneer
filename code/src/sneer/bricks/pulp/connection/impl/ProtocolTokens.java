package sneer.bricks.pulp.connection.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.pulp.lang.StringUtils;

interface ProtocolTokens {

	static final byte[] FALLBACK = my(StringUtils.class).toByteArray("Fallback");
	static final byte[] OK = my(StringUtils.class).toByteArray("OK");
	static final byte[] SNEER_WIRE_PROTOCOL_1 = my(StringUtils.class).toByteArray("Sneer Wire Protocol 1");

}
