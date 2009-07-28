package sneer.bricks.softwaresharing.gui.impl;

import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;

abstract class Util {
	
	static boolean isBrickStagedForExecution(BrickInfo brickInfo) {
		for (BrickVersion version : brickInfo.versions()) 
			if(version.isStagedForExecution())
				return true;
			
		return false;
	}
}
