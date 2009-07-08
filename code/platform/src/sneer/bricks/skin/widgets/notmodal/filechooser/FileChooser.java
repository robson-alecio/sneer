package sneer.bricks.skin.widgets.notmodal.filechooser;

import java.io.File;

import javax.swing.JFileChooser;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface FileChooser {

	JFileChooser newFileChooser(Consumer<File> selectedFile);
	
}
