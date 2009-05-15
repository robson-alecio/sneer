package sneer.pulp.log.formatter;

import sneer.brickness.Brick;

@Brick
public interface LogFormatter {
	
	String format(String message, Object... messageInsets);
	String formatShort(Throwable throwable, String message, Object... messageInsets);
	String format(Throwable throwable, String message, Object... messageInsets) ;
	String format(Throwable throwable);

}
