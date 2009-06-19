package sneer.bricks.hardware.io.codecs.base64.impl;

import sneer.bricks.hardware.io.codecs.base64.Base64;

class Base64Impl implements Base64 {

	public String encode(final byte[] bytes) {
		return PublicDomainBase64.encodeBytes(bytes);
	}

	public String encode(String text) {
		return encode(text.getBytes());
	}

}
