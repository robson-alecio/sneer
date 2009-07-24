package sneer.bricks.pulp.crypto;


public interface Digester {

	void update(byte[] bytes);

	Sneer1024 digest();

}
