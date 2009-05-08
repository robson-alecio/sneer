package sneer.hardware.io.codecs.base64;

import sneer.brickness.Brick;

@Brick
public interface Base64 {

	String encode(final byte[] bytes);

	String encode(String text);

}