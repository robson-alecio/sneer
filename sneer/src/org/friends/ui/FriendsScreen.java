package org.friends.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FriendsScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private final FriendsModel _model;

	public FriendsScreen(FriendsModel model) {

		if (model == null) {
			throw new InvalidParameterException("Model must not be null");
		}

		_model = model;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());

		DefaultListModel friendsListModel = new DefaultListModel();

		JPanel editPanel = new JPanel();

		JTextField nameText = new JTextField();
		editPanel.setLayout(new BorderLayout());
		editPanel.add(nameText, BorderLayout.CENTER);
		editPanel.add(createAddButton(nameText, friendsListModel),
				BorderLayout.EAST);

		this.add(new JList(friendsListModel), BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle("Amigos");
		setSize(200, 400);
	}

	private JButton createAddButton(final JTextField nameText,
			final DefaultListModel friendsListModel) {
		JButton addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				_model.addFriend(nameText.getText());

				refreshFriendsList(friendsListModel);
			}

		});
		return addButton;
	}

	private void refreshFriendsList(final DefaultListModel friendsListModel) {
		friendsListModel.removeAllElements();
		for (String friend : _model.friends()) {
			friendsListModel.addElement(friend);
		}
	}

	public static void main(String[] args) {
		new FriendsScreen(new FriendsModelImpl()).setVisible(true);
	}

}