package sneer.bricks.skin.notmodal.filechooser.multiple;

import java.awt.Component;
import java.io.File;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface MultipleFileChooser{

	void chooseTwoFiles(Component focusComponent, Consumer<File[]> twoFiles);	
}
