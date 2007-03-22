package org.friends.ui;

public interface FriendsModel {

	/**
	 * @param name
	 *            REQUIRED
	 */
	void addFriend(String name);

	String[] friends();

}