package sneer.bricks.softwaresharing.gui.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.bricks.softwaresharing.BrickInfo;

class RootTreeNode extends AbstractTreeNodeWrapper<BrickInfo> {

	private final List<BrickInfo> _infos;

	RootTreeNode(List<BrickInfo> infos) {
		super(null, null);
		_infos = infos;
	}
	
	@Override public String toString() {  return "root"; }
	
	@Override protected List<BrickInfo> listChildren() { 
		Collections.sort(_infos, new Comparator<BrickInfo>(){ @Override public int compare(BrickInfo brick1, BrickInfo brick2) {
			
			if(brick1.status().ordinal()==brick2.status().ordinal())
				return brick1.name().compareTo(brick2.name());
			
			return brick1.status().ordinal()-brick2.status().ordinal();
		}});
		return _infos; 
	}
	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickInfoTreeNode(this, listChildren().get(childIndex));
	}
}
