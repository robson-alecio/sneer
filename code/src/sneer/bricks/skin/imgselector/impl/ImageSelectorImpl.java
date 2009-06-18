package sneer.bricks.skin.imgselector.impl;

import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import sneer.bricks.skin.imgselector.ImageSelector;
import sneer.foundation.lang.Consumer;

class ImageSelectorImpl implements ImageSelector {
	
	private static ImageDialog _dialog;
	
	private File selectFile() {
		JFileChooser fileChooser = new JFileChooser(".");
		ImagePreviewAccessory accessory = new ImagePreviewAccessory(fileChooser);
		fileChooser.setAccessory(accessory);
		fileChooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, accessory);
		
		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	private void showImage(File file, Consumer<Image> imageSetter) {
		_dialog = new ImageDialog(file, imageSetter);
		_dialog.setVisible(true);
	}

	@Override
	public void open(Consumer<Image> imageSetter) {
		File file = selectFile();
		if (file==null) return;
		if (!file.exists()) return;
		
		if(_dialog!=null){
			_dialog.setVisible(false);
			_dialog.dispose();
		}
		
		showImage(file, imageSetter);
	}
}

class ImagePreviewAccessory extends JLabel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 200;

	public ImagePreviewAccessory(JFileChooser chooser) {
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		chooser.addPropertyChangeListener(this);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}

	public void propertyChange(PropertyChangeEvent changeEvent) {
		String changeName = changeEvent.getPropertyName();
		if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			File file = (File) changeEvent.getNewValue();
			if (file != null) {
				ImageIcon icon = new ImageIcon(file.getPath());
				if (icon.getIconWidth() > PREFERRED_WIDTH) {
					icon = new ImageIcon(icon.getImage().getScaledInstance(
							PREFERRED_WIDTH - 20, -1, Image.SCALE_DEFAULT));

					if (icon.getIconHeight() > PREFERRED_HEIGHT) {
						icon = new ImageIcon(icon.getImage()
								.getScaledInstance(-1, PREFERRED_HEIGHT,
										Image.SCALE_DEFAULT));
					}
				}
				setIcon(icon);
			}
		}
	}
}