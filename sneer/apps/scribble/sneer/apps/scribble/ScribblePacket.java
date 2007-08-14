package sneer.apps.scribble;

public interface ScribblePacket {
	
	public static final int BRUSH = 0;
	public static final int CLEAR = 1;
	public static final int COLOR = 2;
	public static final int STROKE = 3;
	
	public int type();

}
