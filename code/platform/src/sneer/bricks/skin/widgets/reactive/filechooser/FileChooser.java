package sneer.bricks.skin.widgets.reactive.filechooser;

import java.io.File;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface FileChooser {

	void choose(Consumer<File> selectedFile);
	
}
