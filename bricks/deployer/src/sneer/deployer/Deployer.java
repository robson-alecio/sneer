package sneer.deployer;

public interface Deployer {
	
	<T> T deploy(String brickName);
	
}
