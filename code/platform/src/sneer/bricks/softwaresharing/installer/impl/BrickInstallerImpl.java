package sneer.bricks.softwaresharing.installer.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickSpace;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.installer.BrickInstaller;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class BrickInstallerImpl implements BrickInstaller {

	@Override
	public void commitStagedBricksInstallation() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void prepareStagedBricksInstallation() {
		for(BrickInfo brickInfo: my(BrickSpace.class).availableBricks())
			for (BrickVersion version : brickInfo.versions())
				if (version.isStagedForExecution())
					prepareStagedVersion(version);
	}

	private void prepareStagedVersion(@SuppressWarnings("unused") BrickVersion version) {
		//installBricks(tmpSrcFolderContainingFilesFor(version));
		throw new NotImplementedYet();
	}

}
