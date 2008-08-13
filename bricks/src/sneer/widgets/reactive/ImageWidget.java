package sneer.widgets.reactive;

import java.awt.Component;
import java.awt.Image;

import javax.swing.JButton;

public interface ImageWidget {

	Image getImage();

	Component getComponent();
	
	JButton getMainWidget();
	
}