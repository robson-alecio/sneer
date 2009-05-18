package sneer.pulp.log.stacktrace;

import sneer.brickness.Brick;

@Brick
public interface LogStackTrace {

	byte[] toByteArray(Throwable throwable);

}
