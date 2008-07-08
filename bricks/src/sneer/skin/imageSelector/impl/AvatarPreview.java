package sneer.skin.imageSelector.impl;
import javax.swing.JFrame;

public class AvatarPreview extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final ImageDialog _imageDialog;
	
	AvatarPreview(ImageDialog imageDialog){
		_imageDialog = imageDialog;
		resizeAvatarPreview();
	}

	void resizeAvatarPreview() {
		int x = 10 + _imageDialog.getLocation().x + _imageDialog.getWidth();
		int y = _imageDialog.getBounds().y;
		setBounds(x,y, 150,150);
	}
}