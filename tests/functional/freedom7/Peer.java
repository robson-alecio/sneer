package functional.freedom7;

public interface Peer {
	
	BrickPublished publishBrick(String string);

	Object lookup(String string);

	void deploy(BrickPublished brick);
	
}
