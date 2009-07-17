package sneer.bricks.software.sharing.gui.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickVersion;

class FakeModel {

	static TreeNode root(){
		
		final List<BrickVersion> versions = new ArrayList<BrickVersion>();
//		versions 
		

		List<BrickInfo> infos = new ArrayList<BrickInfo>();
		infos.add(newBrickInfo("BrickInfo1", versions));
		infos.add(newBrickInfo("BrickInfo2", versions));
		infos.add(newBrickInfo("BrickInfo3", versions));
		infos.add(newBrickInfo("BrickInfo4", versions));
		
		return new RootTreeNode(infos);
	}

	private static BrickInfo newBrickInfo(final String name, final List<BrickVersion> versions) {
		return new BrickInfo(){
			@Override public boolean isSnapp() { return false; }
			@Override public String name() {return name; }
			@Override public List<BrickVersion> versions() { return versions;}
		};
	}
	
	
}
