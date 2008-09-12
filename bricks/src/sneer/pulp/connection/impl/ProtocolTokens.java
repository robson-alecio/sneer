package sneer.pulp.connection.impl;

import wheel.lang.StringUtils;

interface ProtocolTokens {

	static final byte[] FALLBACK = StringUtils.toByteArray("Fallback");
	static final byte[] OK = StringUtils.toByteArray("OK");
	static final byte[] SNEER_WIRE_PROTOCOL_1 = StringUtils.toByteArray("Sneer Wire Protocol 1");

}
