package sneer.pulp.log.stacktrace;

public interface LogStackTrace {

	byte[] toByteArray(Throwable throwable);

}
