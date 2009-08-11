package sneer.bricks.softwaresharing.installer;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickInstaller {

	void prepareStagedBricksInstallation();

	void commitStagedBricksInstallation();

}
