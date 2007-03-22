package org.friends.ui;

import java.util.ArrayList;
import java.util.List;

public class FriendsModelImpl implements FriendsModel {

	private List<String> _friends;

	public FriendsModelImpl() {
		_friends = new ArrayList<String>();
	}

	public void addFriend(String name) {
		_friends.add(name);
	}

	public String[] friends() {
		return _friends.toArray(new String[] {});
	}

}