package sneer.bricks.skin.widgets.reactive.impl;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sneer.bricks.skin.widgets.reactive.ComponentWidget;

abstract class RPanel<WIDGET extends JComponent> extends JPanel implements ComponentWidget<WIDGET>{

	@Override
	public Dimension getMinimumSize() {
		return RUtil.limitSize(super.getMinimumSize());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return RUtil.limitSize(super.getPreferredSize());
	}
	
	@Override
	public Dimension getMaximumSize() {
		return RUtil.limitSize(super.getMaximumSize());
	}

	@Override
	public JComponent getComponent() {
		return this;
	}
}
