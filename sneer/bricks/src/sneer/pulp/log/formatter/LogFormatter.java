package sneer.pulp.log.formatter;

import sneer.brickness.Brick;

@Brick
public interface LogFormatter {
	
	String format(String message, Object... messageInsets);
	String formatShort(Exception e, String message, Object... insets);
	String format(Throwable throwable, String message, Object... messageInsets) ;
	String format(Throwable throwable);

}
