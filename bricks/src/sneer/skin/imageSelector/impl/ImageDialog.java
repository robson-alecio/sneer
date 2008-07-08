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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sneer.skin.image.ImageFactory;

public class ImageDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private ImageFactory _imageFactory;

	private JLabel _image = new JLabel();
	private AvatarPreview _avatarPreview;
	private File _file;

	public ImageDialog(File file, ImageFactory imageFactory) {
		_avatarPreview = new AvatarPreview(this);
		_imageFactory = imageFactory;
		_file = file;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension desktopSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int preferredHeight = (int) (desktopSize.height*0.8);
		int preferredWidth = (int) (desktopSize.width*0.8);
//		setModal(true);
		setBounds((desktopSize.width-preferredWidth)/2,
				(desktopSize.height-preferredHeight)/2,
				preferredWidth,preferredHeight);
		
		ImageIcon icon = getIcon(_file, preferredHeight, preferredWidth);		
		_image.setIcon(icon);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(_image);
		setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
		addSizeListener();
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					setVisible(true);
				}
			}
		);		
	}

	private void addSizeListener() {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				_image.setVisible(false);
				SwingUtilities.invokeLater(
					new Runnable() {
						@Override
						public void run() {
							_image.setIcon(getIcon(_file, getHeight(), getWidth()));
							_image.setVisible(true);
							_avatarPreview.resizeAvatarPreview();
							_avatarPreview.setVisible(true);
						}
					}
				);
			}
			
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				_avatarPreview.resizeAvatarPreview();
			}
		});
	}
	
	private ImageIcon getIcon(final File file, int height, int width) {
		ImageIcon icon = _imageFactory.getIcon(file, false);
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