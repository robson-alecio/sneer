package sneer.bricks.skin.imgselector.impl;
import static sneer.foundation.environments.Environments.my;

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
import javax.swing.WindowConstants;

import sneer.bricks.skin.image.ImageFactory;
import sneer.foundation.lang.Consumer;

class ImageDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	Keyhole _keyhole;

	private File _file;
	private Picture _picture;
	private int _preferredHeight;
	private int _preferredWidth;
	private JLayeredPane _layeredPane;
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);

	private final Consumer<Image> _imageSetter;

    ImageDialog(File file, Consumer<Image> imageSetter) {
    	_file = file;
		_imageSetter = imageSetter;
		_picture = new Picture();
		
		initWindow();
		initLayers();
		addImageInLayer();
		addSizeListener();
		addCloseListener();
		
		_picture.setLocation(0, 0);
		setVisible(true);
	}

	private void addCloseListener() {
		WindowAdapter listener = new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		};
		
		this.addWindowListener(listener);
	}

	private void initWindow() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension desktopSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		_preferredHeight = (int) (desktopSize.height*0.8);
		_preferredWidth = (int) (desktopSize.width*0.8);

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
        _keyhole = new Keyhole(_layeredPane, _imageSetter);
                
    	_layeredPane.add(_keyhole, JLayeredPane.POPUP_LAYER);
    	
    	//resize window
		setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
    }
    
	private void addSizeListener() {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		getContentPane().addComponentListener(new ComponentAdapter() { @Override public void componentResized(ComponentEvent e) {
			_picture.setVisible(false);
			_picture.setIcon(getIcon(_file, getHeight(), getWidth()));
			_picture.setVisible(true);
		}});
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