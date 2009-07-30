package sneer.bricks.softwaresharing.gui.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.bricks.softwaresharing.BrickVersion.Status;

class FakeModel {

	private static long _initialTimeStamp = System.currentTimeMillis();

	static List<BrickInfo> bricks(){
		List<FileVersion> files = new ArrayList<FileVersion>();
		List<BrickInfo> infos = new ArrayList<BrickInfo>();

		files.add(newFileVersion(FakeContent.first(), FakeContent.second(), 
				"Clockjava", sneer.bricks.softwaresharing.FileVersion.Status.MODIFIED));

		files.add(newFileVersion("adsafimww\n222222\n3333333\n44444444\n555555", "adsafimww\n222222\n3333333\n44444444\n555555", 
				"impl/ClockImpl.java", sneer.bricks.softwaresharing.FileVersion.Status.CURRENT));

		files.add(newFileVersion("adsafimww\n222222\n3333333\n44444444\n555555", "", 
				"impl/lib/mylib.jar", sneer.bricks.softwaresharing.FileVersion.Status.MISSING));

		files.add(newFileVersion("", "adsafimww\n222222\n3333333\n44444444\n555555", 
				"impl/lib/otherlib.jar", sneer.bricks.softwaresharing.FileVersion.Status.EXTRA));

		infos.add(newBrickInfo("BrickInfo5", newVersions(files), BrickInfo.Status.DIFFERENT));
		infos.add(newBrickInfo("BrickInfo2", newVersions(files), BrickInfo.Status.NEW));
		infos.add(newBrickInfo("BrickInfo10", newVersions(files), BrickInfo.Status.REJECTED));
		infos.add(newBrickInfo("BrickInfo8", newVersions(files), BrickInfo.Status.DIVERGING));
		infos.add(newBrickInfo("BrickInfo4", newVersions(files), BrickInfo.Status.CURRENT));
		infos.add(newBrickInfo("BrickInfo7", newVersions(files), BrickInfo.Status.DIVERGING));
		infos.add(newBrickInfo("BrickInfo3", newVersions(files), BrickInfo.Status.CURRENT));
		infos.add(newBrickInfo("BrickInfo9", newVersions(files), BrickInfo.Status.REJECTED));
		infos.add(newBrickInfo("BrickInfo1", newVersions(files), BrickInfo.Status.NEW));
		infos.add(newBrickInfo("BrickInfo6", newVersions(files), BrickInfo.Status.DIFFERENT));

		return infos;
	}

	private static List<BrickVersion> newVersions(List<FileVersion> files) {
		List<BrickVersion> versions = new ArrayList<BrickVersion>();
		versions.add(newBrickVersion(Status.CURRENT, 1, files)); 
		versions.add(newBrickVersion(Status.DIVERGING, 2, files)); 
		versions.add(newBrickVersion(Status.DIFFERENT, 20, files)); 
		versions.add(newBrickVersion(Status.REJECTED, 3, files));
		return versions;
	}

	private static FileVersion newFileVersion(final String contents, final String currentContents, 
			final String fileName, final sneer.bricks.softwaresharing.FileVersion.Status status) {
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
			private final List<String> _users = Arrays.asList(new String[]{"User 4", "User 1", "User 3", "User 2"});
			
			@Override public List<FileVersion> files() {return _fileVersions;}
			@Override public boolean isStagedForExecution() {return _staged;}
			@Override public Status status() {return _status; }
			@Override public int unknownUsers() { return _unknownUsersCount; }
			@Override public List<String> knownUsers() {  return  _users;}			
			
			@Override public long publicationDate() { 
				_initialTimeStamp += 1000;
				return _initialTimeStamp;	
			}
			
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

	private static BrickInfo newBrickInfo(final String name, final List<BrickVersion> versions, final BrickInfo.Status status) {
		return new BrickInfo(){
			@Override public boolean isSnapp() { return false; }
			@Override public String name() {return name; }
			@Override public List<BrickVersion> versions() { return versions;}
			@Override public void setStagedForExecution(BrickVersion version, boolean staged) {}
			@Override public BrickInfo.Status status() { return status; }
		};
	}
}

class FakeContent{
	
	static String first(){
		return 	"" + 			"package sneer.bricks.hardware.clock;" +
		
					"\n\n" + 	"import sneer.bricks.pulp.reactive.Signal;" +
					"\n" + 		"import sneer.foundation.brickness.Brick;" +

					"\n" + 		"@Brick" +
					"\n" + 		"public interface Clock {"+
			
					"\n\n\t" + 	"void stop();" +
					"\n\t" + 	"Signal<Date> time();" +
					"\n\t" + 	"void advanceTime(long deltaMillis);" +

					"\n\n" + 	"}";
	}

	static String second(){
		return 	"" + 			"package sneer.bricks.hardware.clock;" +
		
					"\n\n" + 	"import sneer.bricks.pulp.reactive.Signal;" +
					"\n" + 		"import sneer.foundation.brickness.Brick;" +

					"\n" + 		"@Brick" +
					"\n" + 		"public interface Clock {"+
			
					"\n\n\t" + 	"Signal<Long> time();" +
					"\n\t" + 		"void advanceTimeTo(long absoluteTimeMillis);" +
					"\n\t" + 		"void advanceTime(long deltaMillis);" +

					"\n\n" + 	"}";
	}
}