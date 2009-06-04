package spikes.sneer.pulp.deployer;

public class DeployerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeployerException(String message, Throwable t) {
		super(message, t);
	}

	public DeployerException(String message) {
		super(message);
	}


}
