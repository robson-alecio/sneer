package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.lang.Lang.Strings;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheGuide;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;
import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickInfo;

class BrickFetcher implements FileCacheVisitor {

	private final Strings _strings = my(Lang.class).strings();
	
	private final Set<BrickInfo> _bricks = new HashSet<BrickInfo>();
	private final Deque<String> _namePath = new LinkedList<String>();
	private final Deque<Sneer1024> _hashPath = new LinkedList<Sneer1024>();
	
	BrickFetcher(Sneer1024 hashOfAllBricks) {
		
		_namePath.add(null); // placeholder for the root
		_hashPath.add(null);
		
		my(FileClient.class).fetchToCache(hashOfAllBricks);
		my(FileCacheGuide.class).guide(this, hashOfAllBricks);
	}

	
	@Override
	public void visitFileOrFolder(String name, long lastModified, Sneer1024 hashOfContents) {
		_namePath.add(name);
		_hashPath.add(hashOfContents);
	}

	
	@Override
	public void visitFileContents(byte[] fileContents) {
		String fileName = _namePath.peekLast();
		
		_namePath.pop();
		_hashPath.pop();
		
		if (!fileName.endsWith(".java")) return;

		if (!isBrickDefinition(fileContents)) return;
		
		_bricks.add(brickFound());
	}


	private boolean isBrickDefinition(byte[] fileContents) {
		String contents = asString(fileContents);
		return contents.contains("@Brick")
			|| contents.contains("@sneer.foundation.brickness.Brick");
	}


	@Override public void enterFolder() {}
	
	
	@Override public void leaveFolder() {
		_namePath.pop();
		_hashPath.pop();
	}
	
	private BrickInfo brickFound() {
		String fileName = _strings.join(_namePath, ".");
		String brickName = _strings.chomp(fileName, ".java");
		return new BrickInfoImpl(brickName, _hashPath.peekLast());
	}
	
	private String asString(byte[] fileContents) {
		try {
			return new String(fileContents, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	Collection<BrickInfo> bricks() {
		return _bricks;
	}
}
