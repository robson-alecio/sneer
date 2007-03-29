package org.friends.ui;

import wheel.reactive.ListSignal;

public interface FriendsModel {

	void addFriend(String name);
	ListSignal<String> friends();

}