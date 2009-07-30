package sneer.bricks.snapps.diff.text.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanel;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanels;
import sneer.bricks.snapps.diff.text.gui.impl.TextBlockPainter.LinkedTextBlock;
import sneer.bricks.software.diff.TextComparator;
import sneer.bricks.software.diff.TextComparator.TextBlock;
import sneer.bricks.software.diff.TextComparator.TextBlockType;

class TextDiffPanelsImpl implements TextDiffPanels{

	@Override
	public TextDiffPanel newPanel() {
		return new TextDiffPanelImpl();
	}
	
	private static ImageIcon loadIcon(String fileName){
		return my(ImageFactory.class).getIcon(TextDiffPanelsImpl.class, fileName);
	}
	
	class TextDiffPanelImpl extends JPanel implements TextDiffPanel{

		private final JPanel _buttonsPanel = new JPanel();
		private final JTextPane _htmlDif = new JTextPane();
		private final  JScrollPane _scroll = my(SynthScrolls.class).create();
		private final JCheckBox _textOnlyDiff = new JCheckBox("Manual Merge");
		private final  TextBlockPainter _painter = new TextBlockPainter((DefaultStyledDocument) _htmlDif.getStyledDocument(), _textOnlyDiff);
		private String _text1;
		private String _text2;
		
		TextDiffPanelImpl(){
			_htmlDif.setEditable(false);
			setLayout(new BorderLayout());
			add(_scroll, BorderLayout.CENTER);
			add(_buttonsPanel, BorderLayout.SOUTH);
			_scroll.getViewport().add(_htmlDif);	
			
			_buttonsPanel.setLayout(new FlowLayout());
			new SelectionSupport();
			_buttonsPanel.add(_textOnlyDiff);
			
			_textOnlyDiff.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
				compare(_text1, _text2);
			}});
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
			_text1 = text1;
			_text2 = text2;
			TextComparator comparator = my(TextComparator.class);
			List<TextBlock> lastBlocks = comparator.diff(text1, text2);
			lastBlocks = comparator.semanticCleanup(lastBlocks);
			_htmlDif.setText("");
			_painter.append(lastBlocks);
		}

		@Override public Component component() { return this; }
		
		@SuppressWarnings("unused")
		private class SelectionSupport {

			private JButton _nextButton = new JButton(loadIcon("down.png"));
			private JButton _prevButton = new JButton(loadIcon("up.png"));
			
			SelectionSupport(){
				_buttonsPanel.add(_nextButton);
				_buttonsPanel.add(_prevButton);
				initListeners();
			}

			void tryEnableButtons(){
				_nextButton.setEnabled(!_textOnlyDiff.isSelected());
				_prevButton.setEnabled(!_textOnlyDiff.isSelected());
			}
			
			private void initListeners() {
				_textOnlyDiff.addActionListener(new ActionListener(){ 
					
					Object _refToAvoidGc = SelectionSupport.this;
					
					@Override public void actionPerformed(ActionEvent e) {
					tryEnableButtons();
				}});
				
				ActionListener listener = new ActionListener() {
					LinkedTextBlock _current = null;

					@Override
					public void actionPerformed(ActionEvent e) {
						if (_current == null) {
							_current = _painter.root;
							selectText(_current);
							return;
						}

						if (e.getSource() == _nextButton) {
							_current = _current._next;
							selectText(_current);
							return;
						}

						_current = _current._previous;
						selectText(_current);

					}
				};
				_nextButton.addActionListener(listener);
				_prevButton.addActionListener(listener);
			}

			private void selectText(LinkedTextBlock toSelect) {
				_htmlDif.requestFocus();
				changeSelectionColor(toSelect._textBlock.type());
				int[] positions = toSelect.positions();
				_htmlDif.select(positions[0], positions[1]);
			}

			private void changeSelectionColor(TextBlockType type) {
				if (type == TextBlockType.DELETE) {
					_htmlDif.setSelectionColor(Color.RED);
					return;
				}

				if (type == TextBlockType.INSERT) {
					_htmlDif.setSelectionColor(Color.GREEN);
					return;
				}

				_htmlDif.setSelectionColor(Color.LIGHT_GRAY);
			}
		}
	}	
}