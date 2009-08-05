package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.lang.Lang.Strings;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitors;
import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickFetcher implements FileCacheVisitor {

	private final Strings _strings = my(Lang.class).strings();

	Map<String, BrickInfo> _bricksByName = new HashMap<String, BrickInfo>();
	private Deque<String> _currentPath = new LinkedList<String>();
	
	
	BrickFetcher(Sneer1024 hashOfOwnBricks, Sneer1024 hashOfPlatformBricks) {
		fetchBricks(hashOfOwnBricks);
		fetchBricks(hashOfPlatformBricks);
	}

	
	private void fetchBricks(Sneer1024 hash) {
		my(FileClient.class).fetchToCache(hash);
		my(FileCacheVisitors.class).accept(hash, this);
	}

	
	@Override
	public void enterFolderEntry(String entryName, long lastModified) {
		_currentPath.add(entryName);
	}

	
	@Override
	public void leaveFolderEntry() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	
	@Override
	public void visitFileContents(byte[] fileContents) {
		if (!currentFolderEntry().endsWith(".java")) return;
		
		if (!asString(fileContents).contains("@Brick")) return;
		if (_bricksByName.containsKey(currentBrick())) return; //Visiting platform now and brick had already been found in own.
		
		throw new NotImplementedYet();
	}

	
	@Override
	public void visitFolder() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	
	private String currentBrick() {
		String result = _strings.join(_currentPath, ".");
		return _strings.chomp(result, ".java");
	}

	
	private String asString(byte[] fileContents) {
		try {
			return new String(fileContents, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	
	Collection<BrickInfo> bricks() {
		return _bricksByName.values();
	}

	
	private String currentFolderEntry() {
		return _currentPath.peekLast();
	}


}
