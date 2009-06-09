package sneer.bricks.pulp.lang;

import sneer.foundation.brickness.Brick;

@Brick
public interface StringUtils {

	byte[] toByteArray(String string);

	String[] splitRight(String line, char separator, int maxParts);

}