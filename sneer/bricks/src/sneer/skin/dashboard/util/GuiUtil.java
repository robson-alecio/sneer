package sneer.skin.dashboard.util;

import java.awt.Container;
import java.awt.Window;

import sneer.brickness.Brick;

public interface GuiUtil extends Brick {

	void setWindowBounds(Container instrumentContainer, Window window,	int offset, int width);

}
