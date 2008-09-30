package sneer.pulp.keymanager;


public interface PublicKey {

	byte[] bytes();
	
	String toHexa();
}
