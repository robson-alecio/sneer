package sneer.hardware.io.codecs.base64;

public interface Base64 {

	String encode(final byte[] bytes);

	String encode(String text);

}