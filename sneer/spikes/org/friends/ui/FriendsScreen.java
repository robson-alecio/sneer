package org.friends.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import wheel.lang.Threads;

public class FriendsScreen extends JFrame {

	private static final String ADD_FRIEND_BUTTON_TEXT = "+";

	private static final String TITLE = "Amigos";

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
		
		final ListModel<String> friendsListModel = new ListModel<String>();
		int todo;//_model.friends().addReceiver(friendsListModel);
		final List<String> names = new ArrayList<String>();
		friendsListModel.listReplaced(names);
		
		Threads.startDaemon(new Runnable() {		
			public void run() {
				while (true){
					Threads.sleepWithoutInterruptions(2000);
					names.add(""+System.currentTimeMillis());
					friendsListModel.elementAdded(names.size() - 1);
					
					System.out.println("added");
				}
			}
		});
		
		JPanel editPanel = new JPanel();

		JTextField nameText = new JTextField();
		editPanel.setLayout(new BorderLayout());
		editPanel.add(nameText, BorderLayout.CENTER);
		editPanel.add(createAddButton(nameText), BorderLayout.EAST);

		this.add(new JList(friendsListModel), BorderLayout.CENTER);
		this.add(editPanel, BorderLayout.SOUTH);

		setTitle(TITLE);
		setSize(200, 400);
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
	
	public static void main(String[] args) {
		new FriendsScreen(new FriendsModelImpl(null, null));
	}

}