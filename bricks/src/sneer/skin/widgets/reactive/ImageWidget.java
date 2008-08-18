package sneer.skin.widgets.reactive;

import java.awt.Component;
import java.awt.Image;

import javax.swing.JComponent;

public interface ImageWidget {

	Image getImage();

	Component getComponent();
	
	JComponent getMainWidget();
	
}