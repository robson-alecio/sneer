package sneer.skin.imageSelector.impl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ImageDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	Dimension desktopSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int PREFERRED_HEIGHT = (int) (desktopSize.height*0.8);
	int PREFERRED_WIDTH = (int) (desktopSize.width*0.8);
	JLabel image = new JLabel();

	private File _file;

	public ImageDialog(File file) {
		_file = file;
		setModal(true);
		setBounds((desktopSize.width-PREFERRED_WIDTH)/2,
				(desktopSize.height-PREFERRED_HEIGHT)/2,
				PREFERRED_WIDTH,PREFERRED_HEIGHT);
		
		ImageIcon icon = getIcon(_file, PREFERRED_HEIGHT, PREFERRED_WIDTH);		
		image.setIcon(icon);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(image);
		setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));

		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				image.setIcon(getIcon(_file, getHeight(), getWidth()));
			}
		});
	}
	
	private ImageIcon getIcon(final File file, int height, int width) {
		ImageIcon icon = new ImageIcon(file.getPath());
		if (icon.getIconWidth() > width) {
			icon = new ImageIcon(
				icon.getImage().getScaledInstance(
					width, -1, 
					Image.SCALE_DEFAULT));
			
			if (icon.getIconHeight() > height) {
				icon = new ImageIcon(
					icon.getImage().getScaledInstance(-1, 
						height, 
						Image.SCALE_DEFAULT));
			}
		}
		return icon;
	}
}