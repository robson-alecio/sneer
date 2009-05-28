package sneer.installer;

import java.io.File;

import main.Sneer;
import main.SneerStoragePath;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;

public class Runner {

	private File _binDir;

	void start(SneerStoragePath sneerStoragePath) {
		
		_binDir = new File(sneerStoragePath.get(), "bin");
		
		Brickness container = BricknessFactory.newBrickContainer(sneerStoragePath);
		
		placeBricks(container, Sneer.natures());
		placeBricks(container, Sneer.businessBricks());
		placeBricks(container, Sneer.communicationBricks());
	}

	private void placeBricks(Brickness container, Class<?>... bricks) throws BrickLoadingException {
		for (Class<?> brick : bricks)
			container.placeBrick(_binDir, brick.getName());
	}
}
