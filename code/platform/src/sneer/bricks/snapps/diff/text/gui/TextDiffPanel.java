package sneer.bricks.snapps.diff.text.gui;

import java.awt.Component;
import java.io.File;

public interface TextDiffPanel {
	
	void compare(File file1, File file2);
	Component component();

}
