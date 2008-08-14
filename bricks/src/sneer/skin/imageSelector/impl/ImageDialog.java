package sneer.skin.imageSelector.impl;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sneer.skin.image.ImageFactory;
import wheel.lang.Omnivore;

public class ImageDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	AvatarPreview _avatarPreview;
	Keyhole _keyhole;

	private File _file;
	private Picture _picture;
	private int _preferredHeight;
	private int _preferredWidth;
	private JLayeredPane _layeredPane;
	private ImageFactory _imageFactory;


    ImageDialog(File file, ImageFactory imageFactory, Omnivore<Image> imageSetter) {
    	_file = file;
    	_imageFactory = imageFactory;
		_avatarPreview = new AvatarPreview(this, imageFactory, imageSetter);
		_picture = new Picture(_avatarPreview);
		
		initWindow();
		initLayers();
		addImageInLayer();
		addSizeListener();
		addCloseListener();
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					_picture.setLocation(0, 0);
					setVisible(true);
				}
			}
		);		
	}

	private void addCloseListener() {
		WindowAdapter listener = new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				_avatarPreview.setVisible(false);
				_avatarPreview.dispose();
				setVisible(false);
				dispose();
			}
		};
		
		this.addWindowListener(listener);
		_avatarPreview.addWindowListener(listener);
	}

	private void initWindow() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension desktopSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		_preferredHeight = (int) (desktopSize.height*0.8);
		_preferredWidth = (int) ((desktopSize.width-AvatarPreview._WIDTH)*0.95);

		setBounds((desktopSize.width-_preferredWidth)/2,
				(desktopSize.height-_preferredHeight)/2,
				_preferredWidth,_preferredHeight);
	}

	private void initLayers() {
		_layeredPane = new JLayeredPane();
		getContentPane().add(_layeredPane);
	}

    private void addImageInLayer() {
		ImageIcon icon = getIcon(_file, _preferredHeight, _preferredWidth);		
		_picture.setIcon(icon);
		_layeredPane.setLayout(new FlowLayout());    
        _layeredPane.add(_picture, JLayeredPane.DEFAULT_LAYER);
        _keyhole = new Keyhole(_layeredPane, _avatarPreview);
        
        _avatarPreview._area.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				SwingUtilities.invokeLater(
					new Runnable() {
						@Override
						public void run() {
							int value = _avatarPreview._area.getValue();
							_keyhole.setPreferredSize(new Dimension(value,value));
							_keyhole.invalidate();
							_keyhole.getParent().validate();
							_keyhole.repaint();
						}
					}
				);	
			}}
		);
        
    	_layeredPane.add(_keyhole, JLayeredPane.POPUP_LAYER);
    	
    	//resize window
		setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
    }
    
	private void addSizeListener() {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				_picture.setVisible(false);
				_picture.setIcon(getIcon(_file, getHeight(), getWidth()));
				_avatarPreview.resizeAvatarPreview();
				SwingUtilities.invokeLater(
					new Runnable() {
						@Override
						public void run() {
							_picture.setVisible(true);
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
		ImageIcon icon = _imageFactory.getIcon(file);
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