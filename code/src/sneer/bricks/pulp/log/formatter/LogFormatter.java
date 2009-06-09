package sneer.bricks.pulp.log.formatter;

import sneer.foundation.brickness.Brick;

@Brick
public interface LogFormatter {
	
	String format(String message, Object... messageInsets);
	String formatShort(Throwable throwable, String message, Object... messageInsets);
	String format(Throwable throwable, String message, Object... messageInsets) ;
	String format(Throwable throwable);

}
