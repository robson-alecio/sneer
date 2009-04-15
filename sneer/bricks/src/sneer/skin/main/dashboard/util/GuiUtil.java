package sneer.skin.main.dashboard.util;

import java.awt.Container;
import java.awt.Window;

import sneer.brickness.OldBrick;

public interface GuiUtil extends OldBrick {

	void setWindowBounds(Container instrumentContainer, Window window,	int offset, int width);

}
