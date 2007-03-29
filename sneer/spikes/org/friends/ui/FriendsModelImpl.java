package org.friends.ui;

import java.util.ArrayList;
import java.util.List;

import wheel.io.ui.TrayIcon.Action;
import wheel.reactive.ListSignal;

public class FriendsModelImpl implements FriendsModel {
	public interface AddFriendAction {
		void addFriend(String name);
	}

	private final ListSignal<String> _friendsList;
	private final AddFriendAction _addFriendAction;

	public FriendsModelImpl(ListSignal<String> friendsList, AddFriendAction addFriendAction) {
		_friendsList = friendsList;
		_addFriendAction = addFriendAction;
	}

	public void addFriend(String name) {
		_addFriendAction.addFriend(name);
	}

	public ListSignal<String> friends() {
		return _friendsList;
	}

}