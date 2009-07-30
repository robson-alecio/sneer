package sneer.bricks.softwaresharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;

import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickSpace;

class RootTreeNode extends AbstractTreeNodeWrapper<BrickInfo> {

	private List<BrickInfo> _infos;

	RootTreeNode(List<BrickInfo> infos) {
		super(null, null);
		_infos = infos;
		sortBriks();
	}

	RootTreeNode() {
		super(null, null);
		load();		
	}

	void load() {
		_infos = new ArrayList<BrickInfo>();
		Collection<BrickInfo> currentElements = my(BrickSpace.class).availableBricks().currentElements();
		_infos.addAll(currentElements);
		System.out.println(currentElements.size());
		sortBriks();
	}

	private void sortBriks() {
		Comparator<BrickInfo> comparator = new Comparator<BrickInfo>(){ @Override public int compare(BrickInfo brick1, BrickInfo brick2) {
			if(brick1.status().ordinal()==brick2.status().ordinal())
				return brick1.name().compareTo(brick2.name());
		
			return brick1.status().ordinal()-brick2.status().ordinal();
		}};
		Collections.sort(_infos, comparator );
	}
	
	@Override public String toString() {  return "root"; }
	
	@Override protected List<BrickInfo> listChildren() { 

		return _infos; 
	}
	@SuppressWarnings("unchecked")
	@Override protected AbstractTreeNodeWrapper wrapChild(int childIndex) {
		return new BrickInfoTreeNode(this, listChildren().get(childIndex));
	}

	@Override public ImageIcon getIcon() { return null; }
}
