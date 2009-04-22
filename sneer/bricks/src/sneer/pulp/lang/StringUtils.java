package sneer.pulp.lang;

import sneer.brickness.Brick;

@Brick
public interface StringUtils {

	byte[] toByteArray(String string);

	String[] splitRight(String line, char separator, int maxParts);

}