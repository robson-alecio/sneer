package sneer.bricks.hardware.io.log.formatter;

import sneer.foundation.brickness.Brick;

@Brick
public interface LogFormatter {
	
	String format(String message, Object... messageInsets);

}
