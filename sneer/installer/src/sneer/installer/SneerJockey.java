package sneer.installer;

import java.io.File;

import main.Sneer;
import main.SneerStoragePath;
import sneer.brickness.BrickPlacementException;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;

/** This guy "plays" (runs) the latest version of Sneer, one after the other. */
public class SneerJockey {

	private final File _binDir;
	private final SneerStoragePath _sneerStoragePath;

	public SneerJockey(SneerStoragePath sneerStoragePath) throws Exception {
		_sneerStoragePath = sneerStoragePath;
		_binDir = new File(sneerStoragePath.get(), "bin");
		
//		while (true)
			play();
	}

	private void play() throws Exception {
		//TODO: add class loader
		Brickness container = BricknessFactory.newBrickContainer(_sneerStoragePath);
		placeBricks(container, Sneer.natures());
		placeBricks(container, Sneer.businessBricks());
		placeBricks(container, Sneer.communicationBricks());
	}

	private void placeBricks(Brickness container, Class<?>... bricks) throws BrickPlacementException {
		for (Class<?> brick : bricks)
			container.placeBrick(_binDir, brick.getName());
	}
}