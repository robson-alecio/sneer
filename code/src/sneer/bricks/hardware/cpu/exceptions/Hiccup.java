package sneer.bricks.hardware.cpu.exceptions;

/**
 * Marker used to indicate that a method might take more than one
 * microsecond to return. Never actually thrown.
 */
public abstract class Hiccup extends Throwable {

}
