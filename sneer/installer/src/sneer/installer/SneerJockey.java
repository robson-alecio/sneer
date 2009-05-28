package sneer.installer;

import static sneer.commons.environments.Environments.my;
import main.Sneer;
import main.SneerStoragePath;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;

/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	private final SneerStoragePath _sneerStoragePath;

	public SneerJockey(SneerStoragePath sneerStoragePath) throws Exception {
		_sneerStoragePath = sneerStoragePath;
		
//		while (true)
			play();
	}

	private void play() throws Exception {
		//TODO: add class loader:  new File(sneerStoragePath.get(), "bin");
		Environment container = Brickness.newBrickContainer(_sneerStoragePath);
		loadBricks(container, Sneer.businessBricks());
		loadBricks(container, Sneer.communicationBricks());
	}

	private void loadBricks(Environment container, final Class<?>... bricks) throws BrickLoadingException {
		Environments.runWith(container, new Runnable() { @Override public void run() {
			for (Class<?> brick : bricks) my(brick);
		}});
	}
}