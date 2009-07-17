package sneer.bricks.software.sharing.gui.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickVersion;
import sneer.bricks.software.sharing.FileVersion;
import sneer.bricks.software.sharing.BrickVersion.Status;

class FakeModel {

	static TreeNode root(){
		List<FileVersion> _fileVersions = new ArrayList<FileVersion>();
		List<BrickVersion> versions = new ArrayList<BrickVersion>();
		List<BrickInfo> infos = new ArrayList<BrickInfo>();

		versions.add(newBrickVersion(Status.CURRENT, 1, _fileVersions)); 
		versions.add(newBrickVersion(Status.DIFFERENT, 2, _fileVersions)); 
		versions.add(newBrickVersion(Status.REJECTED, 1, _fileVersions)); 

		infos.add(newBrickInfo("BrickInfo1", versions));
		infos.add(newBrickInfo("BrickInfo2", versions));
		infos.add(newBrickInfo("BrickInfo3", versions));
		infos.add(newBrickInfo("BrickInfo4", versions));
		
		return new RootTreeNode(infos);
	}

	private static BrickVersion newBrickVersion(final Status status, final int _unknownUsersCount, final List<FileVersion> _fileVersions) {
		return new BrickVersion(){ 

			private boolean _staged;
			private Status _status = status;
			
			@Override public List<FileVersion> files() {return _fileVersions;}
			@Override public boolean isStagedForExecution() {return _staged;}
			@Override public List<String> knownUsers() { return Arrays.asList(new String[]{"User 1", "User 2", "User 3", "User 4"}); }
			@Override public long publicationDate() { return System.currentTimeMillis();	}
			@Override public void setStagedForExecution(boolean staged) { _staged = staged; }
			@Override public Status status() {return _status; }
			@Override public int unknownUsers() { return _unknownUsersCount; }
			@Override public void setRejected(boolean rejected) { 
				if(rejected) {
					_status = Status.REJECTED;
					return;
				}
				_status = Status.DIFFERENT;
			}
		};
	}

	private static BrickInfo newBrickInfo(final String name, final List<BrickVersion> versions) {
		return new BrickInfo(){
			@Override public boolean isSnapp() { return false; }
			@Override public String name() {return name; }
			@Override public List<BrickVersion> versions() { return versions;}
		};
	}
}
