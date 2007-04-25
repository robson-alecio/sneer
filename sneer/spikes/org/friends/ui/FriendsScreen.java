package org.friends.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.InvalidParameterException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sneer.kernel.business.Contact;

public class FriendsScreen extends JFrame {

	private static final String TITLE = "Amigos";
	private static final String ADD_FRIEND_BUTTON_TEXT = "+";
	private static final String FRIEND_MENU_REMOVE_TEXT = "remover";

	private static final long serialVersionUID = 1L;

	private final FriendsModel _model;

	public FriendsScreen(FriendsModel model) {

		if (model == null) {
			throw new InvalidParameterException("Model must not be null");
		}

		_model = model;
		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		
		final JTextField nameText = new JTextField();
		makeNameTextTheFirstFocusComponent(nameText);
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		editPanel.add(nameText, BorderLayout.CENTER);
		editPanel.add(createAddButton(nameText), BorderLayout.EAST);

		this.add(new JScrollPane(createFriendsList()), BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle(TITLE);
		setSize(200, 400);
	}

	private void makeNameTextTheFirstFocusComponent(
			final JTextField nameText) {
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy(){
			@Override
			public Component getFirstComponent(Container arg0) {
				return nameText;
			}
		
			private static final long serialVersionUID = 1L;});
	}

	private JList createFriendsList() {
		final ListModel<Contact> friendsListModel = new ListModel<Contact>();
		final JList friendsList = new JList(friendsListModel);
		_model.friends().addListReceiver(friendsListModel);
		
		friendsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (rightClick){
					int indexUnderMouse = friendsList.locationToIndex(mouseEvent.getPoint());
					if (indexUnderMouse == -1)
						return;
					
					friendsList.setSelectedIndex(indexUnderMouse);
					
					getFriendPopUpMenu(friendsList)
						.show(friendsList, mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
		return friendsList;
	}

	private JPopupMenu getFriendPopUpMenu(final JList friendsList) {
		final JPopupMenu friendMenu = new JPopupMenu();
		friendMenu.add(getRemoveFriendMenuItem(friendsList));
		return friendMenu;
	}

	private JMenuItem getRemoveFriendMenuItem(final JList friendsList) {
		final JMenuItem removeFriendMenuItem = new JMenuItem(FRIEND_MENU_REMOVE_TEXT);
		removeFriendMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				Contact selectedFriend = (Contact)friendsList.getSelectedValue();
				int answer = JOptionPane.showConfirmDialog(FriendsScreen.this, 
						"Deseja remover o amigo " + selectedFriend + "?");
				if (answer == JOptionPane.YES_OPTION){
					_model.removeFriend(selectedFriend);
				}
			}
		});
		return removeFriendMenuItem;
	}

	private JButton createAddButton(final JTextField nameText) {
		JButton addButton = new JButton(ADD_FRIEND_BUTTON_TEXT);
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				_model.addFriend(nameText.getText());
			}

		});
		return addButton;
	}
	
}