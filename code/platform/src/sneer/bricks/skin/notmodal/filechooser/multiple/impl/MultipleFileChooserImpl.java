package sneer.bricks.skin.notmodal.filechooser.multiple.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import sneer.bricks.skin.notmodal.filechooser.FileChoosers;
import sneer.bricks.skin.notmodal.filechooser.multiple.MultipleFileChooser;
import sneer.foundation.lang.Consumer;

public class MultipleFileChooserImpl implements MultipleFileChooser {
	
	@Override public void chooseTwoFiles(final Component focusComponent, final Consumer<File[]> twoFiles) {
		new Consumer<File>(){	
			JFileChooser _chooser = my(FileChoosers.class).newFileChooser(this);
			{ showChooser(); }

			private File _file1;
			
			@Override public void consume(File value) {
				if(value==null) {
					_file1 = null;
					return;
				}
				
				if(_file1==null){
					_file1 = value;
					showChooser();
					return;
				}
				
				File tmp = _file1;
				_file1 = null;
				twoFiles.consume(new File[]{tmp, value});
			}
			
			private void showChooser() {
				_chooser.showOpenDialog(focusComponent);
			}
		};
	}
}
