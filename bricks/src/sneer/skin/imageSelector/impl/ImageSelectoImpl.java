package sneer.skin.imageSelector.impl;

import java.io.File;

import javax.swing.JFileChooser;

import sneer.skin.imageSelector.ImageSelector;

public class ImageSelectoImpl implements ImageSelector {

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

	private void showImage(final File file) {

		ImageDialog dlg = new ImageDialog(file);
		dlg.setVisible(true);
	}

	public static void main(String[] args) {
		ImageSelectoImpl is = new ImageSelectoImpl();
		is.showImage(is.selectFile());
	}

}