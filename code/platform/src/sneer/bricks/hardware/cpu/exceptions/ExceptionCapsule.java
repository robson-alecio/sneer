package sneer.bricks.hardware.cpu.exceptions;



public class ExceptionCapsule extends RuntimeException {

	public ExceptionCapsule(Exception e) {
		super(e);
		if (e == null) throw new IllegalArgumentException();
	}

}
