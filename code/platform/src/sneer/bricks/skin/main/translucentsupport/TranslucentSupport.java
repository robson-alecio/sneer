package sneer.bricks.skin.main.translucentsupport;

import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Window;

import sneer.foundation.brickness.Brick;

@Brick
public interface TranslucentSupport {

	Object perpixelTransparent();
	Object perpixelTranslucent();
	Object translucent();
	
	boolean isTranslucencySupported(Object kind);
	boolean isTranslucencyCapable(GraphicsConfiguration gc);
	
	void setWindowShape(Window window, Shape shape);
	void setWindowOpaque(Window window, boolean opaque);
	void setWindowOpacity(Window window, float opacity);

}
