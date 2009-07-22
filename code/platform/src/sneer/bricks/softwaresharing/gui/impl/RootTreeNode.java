package sneer.bricks.softwaresharing.gui.impl;

import java.util.List;

import sneer.bricks.softwaresharing.BrickInfo;

class RootTreeNode extends AbstractTreeNodeWrapper<BrickInfo> {

	private final List<BrickInfo> _infos;

	RootTreeNode(List<BrickInfo> infos) {
		super(null, null);
		_infos = infos;
	}
	
	@Override public String toString() {  return "root"; }
	@Override protected List<BrickInfo> listChildren() { return _infos; }

	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickInfoTreeNode(this, listChildren().get(childIndex));
	}
}
