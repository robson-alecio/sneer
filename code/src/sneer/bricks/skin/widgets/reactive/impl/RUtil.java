package sneer.bricks.skin.widgets.reactive.impl;

import java.awt.Dimension;

abstract class RUtil {

	static final int _MinimumSize = 20;

	static Dimension limitSize(Dimension size){
		if(size.getHeight()<_MinimumSize)
			size.setSize(size.getWidth(), _MinimumSize);
		
		if(size.getWidth()<_MinimumSize)
			size.setSize(_MinimumSize,size.getHeight());
		
		return size;
	}
}