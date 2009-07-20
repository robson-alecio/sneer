package sneer.bricks.snapps.diff.text.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanels;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanel;
import sneer.bricks.software.diff.TextComparator;
import sneer.bricks.software.diff.TextComparator.TextBlock;

class TextDiffPanelsImpl implements TextDiffPanels{

	@Override
	public TextDiffPanel newPanel() {
		return new TextDiffPanelImpl();
	}
	
	class TextDiffPanelImpl extends JPanel implements TextDiffPanel{

		private final JTextPane _htmlDif = new JTextPane();
		private final  JScrollPane _scroll = my(SynthScrolls.class).create();
		private final  TextBlockPainter _painter = new TextBlockPainter((DefaultStyledDocument) _htmlDif.getStyledDocument());
		
		TextDiffPanelImpl(){
			_htmlDif.setEditable(false);
			setLayout(new BorderLayout());
			add(_scroll, BorderLayout.CENTER);
			_scroll.getViewport().add(_htmlDif);			
		}
		
		@Override public void compare(File file1, File file2) {
			String text1;
			String text2;
			try {
				text1 = read(file1);
				text2 = read(file2);
			} catch (IOException ignore) {
				_htmlDif.setText(ignore.getMessage());
				return;
			}
			compare(text1, text2);
		}
		
		private String read(File file) throws IOException {
			try {
				return my(IO.class).files().readString(file);
			} catch (IOException e) {
				my(BlinkingLights.class).turnOn(LightType.ERROR, "Error", "Unable to read file: " + file.getAbsolutePath(),  e, 5*60*1000);
				throw e;
			}
		}

		@Override public void compare(String text1, String text2) {
			TextComparator comparator = my(TextComparator.class);
			Iterator<TextBlock> blocksIterator = comparator.diff(text1, text2);
			blocksIterator = comparator.semanticCleanup(blocksIterator);
			_htmlDif.setText("");
			_painter.append(blocksIterator);
		}
		
		@Override public Component component() { return this; }
	}
}