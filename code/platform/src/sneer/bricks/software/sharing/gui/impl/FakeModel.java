package sneer.bricks.software.sharing.gui.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.TreeNode;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.software.sharing.BrickInfo;
import sneer.bricks.software.sharing.BrickVersion;
import sneer.bricks.software.sharing.FileVersion;
import sneer.bricks.software.sharing.BrickVersion.Status;

class FakeModel {

	static TreeNode root(){
		List<FileVersion> files = new ArrayList<FileVersion>();
		List<BrickVersion> versions = new ArrayList<BrickVersion>();
		List<BrickInfo> infos = new ArrayList<BrickInfo>();

		files.add(newFileVersion("3333333\nadsafimww\n222222\n555555\n6666666", "adsafimww\n222222\n3333333\n44444444\n555555", 
				"MODIFIED", sneer.bricks.software.sharing.FileVersion.Status.MODIFIED));
		
		files.add(newFileVersion("adsafimww\n222222\n3333333\n44444444\n555555", "adsafimww\n222222\n3333333\n44444444\n555555", 
				"CURRENT", sneer.bricks.software.sharing.FileVersion.Status.CURRENT));
		
		files.add(newFileVersion("adsafimww\n222222\n3333333\n44444444\n555555", "", 
				"MISSING", sneer.bricks.software.sharing.FileVersion.Status.MISSING));

		files.add(newFileVersion("", "adsafimww\n222222\n3333333\n44444444\n555555", 
				"EXTRA", sneer.bricks.software.sharing.FileVersion.Status.EXTRA));
		
		versions.add(newBrickVersion(Status.CURRENT, 1, files)); 
		versions.add(newBrickVersion(Status.DIFFERENT, 2, files)); 
		versions.add(newBrickVersion(Status.REJECTED, 1, files)); 

		infos.add(newBrickInfo("BrickInfo1", versions, sneer.bricks.software.sharing.BrickInfo.Status.NEW));
		infos.add(newBrickInfo("BrickInfo2", versions, sneer.bricks.software.sharing.BrickInfo.Status.CURRENT));
		infos.add(newBrickInfo("BrickInfo3", versions, sneer.bricks.software.sharing.BrickInfo.Status.DIFFERENT));
		infos.add(newBrickInfo("BrickInfo4", versions, sneer.bricks.software.sharing.BrickInfo.Status.REJECTED));
		
		return new RootTreeNode(infos);
	}

	private static FileVersion newFileVersion(final String contents, final String currentContents, 
			final String fileName, final sneer.bricks.software.sharing.FileVersion.Status status) {
		return new FileVersion(){ 
			@Override public byte[] contents() {  return contents.getBytes(); }
			@Override public byte[] contentsInCurrentVersion() { 	return currentContents.getBytes(); }
			@Override public String name() { return fileName; }
			@Override public Status status() { return status; }
		};
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
			@Override public File sourceFolder() {
				throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
			}
			@Override public Sneer1024 hash() {
				throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
			}
		};
	}

	private static BrickInfo newBrickInfo(final String name, final List<BrickVersion> versions, final sneer.bricks.software.sharing.BrickInfo.Status status) {
		return new BrickInfo(){
			@Override public boolean isSnapp() { return false; }
			@Override public String name() {return name; }
			@Override public List<BrickVersion> versions() { return versions;}
			@Override public sneer.bricks.software.sharing.BrickInfo.Status status() { return status; }
		};
	}
}
