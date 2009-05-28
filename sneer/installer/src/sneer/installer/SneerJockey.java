package sneer.installer;

import main.Sneer;
import main.SneerStoragePath;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;

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
		Brickness container = BricknessFactory.newBrickContainer(_sneerStoragePath);
		loadBricks(container, Sneer.businessBricks());
		loadBricks(container, Sneer.communicationBricks());
	}

	private void loadBricks(Brickness container, Class<?>... bricks) throws BrickLoadingException {
		for (Class<?> brick : bricks)
			container.environment().provide(brick);
	}
}