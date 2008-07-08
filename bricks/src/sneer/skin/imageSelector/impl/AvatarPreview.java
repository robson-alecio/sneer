package sneer.skin.imageSelector.impl;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class AvatarPreview extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final ImageDialog _imageDialog;
	
	AvatarPreview(ImageDialog imageDialog){
		_imageDialog = imageDialog;
		resizeAvatarPreview();
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					JScrollPane scroll = new JScrollPane();
					getContentPane().add(scroll);
					
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
					scroll.getViewport().add(panel);
					
					panel.add(new JCheckBox("Crop Image"));
					panel.add(new JSeparator());
					
					addSizeCheck(panel, 24);
					addSizeCheck(panel, 32);
					addSizeCheck(panel, 48);
					addSizeCheck(panel, 64);
					addSizeCheck(panel, 128);
				}

				private void addSizeCheck(JPanel panel, int size) {
					panel.add(new JCheckBox(size+"x"+size));
					MyLabel button = new MyLabel(size);
					panel.add(button);
					panel.add(new JSeparator());
				}
			}
		);			
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x,y, 180,_imageDialog.getHeight());
	}
}

class MyLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	private final Dimension _size;

	MyLabel(int size){
		_size = new Dimension(size,size);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return _size;
	}

	@Override
	public Dimension getMaximumSize() {
		return _size;
	}

	@Override
	public Dimension getMinimumSize() {
		return _size;
	}
}