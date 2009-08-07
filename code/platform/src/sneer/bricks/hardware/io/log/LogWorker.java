package sneer.bricks.hardware.io.log;

public interface LogWorker {

	void log(String message, Object... messageInsets);

}
