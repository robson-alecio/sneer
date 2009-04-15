package sneer.skin.main.dashboard.util.impl;

import java.awt.Container;
import java.awt.Point;
import java.awt.Window;

import sneer.skin.main.dashboard.util.GuiUtil;

class GuiUtilImpl implements GuiUtil {

	@Override
	public void setWindowBounds(Container instrumentContainer, Window window, int offset, int width) {
		int windowHeight = instrumentContainer.getBounds().height+offset;
		Point location = instrumentContainer.getLocationOnScreen();
		int y = location.y-offset;
		int x = location.x;
		
		window.setBounds(x - width - 20, y, width, windowHeight);
	}

}