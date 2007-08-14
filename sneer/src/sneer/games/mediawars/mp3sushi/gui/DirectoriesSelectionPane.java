package sneer.games.mediawars.mp3sushi.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class DirectoriesSelectionPane extends JPanel implements
		ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList list;
	private DefaultListModel listModel;

	private static final String addString = "Add Dir";
	private static final String removeString = "Remove Dir";
	private JButton removeButton;
	private JButton addButton;
	private User _user;

	public DirectoriesSelectionPane(User user) {
		super(new BorderLayout());
		_user = user;

		listModel = new DefaultListModel();

		// Create the list and put it in a scroll pane.
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
//		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);

		addButton = new JButton(addString);
		AddListener addListener = new AddListener();
		addButton.setActionCommand(addString);
		addButton.addActionListener(addListener);

		removeButton = new JButton(removeString);
		removeButton.setActionCommand(removeString);
		removeButton.addActionListener(new RemoveListener());

		// Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(addButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(removeButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	}

	private File chooseTargetDirectory() throws CancelledByUser {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setApproveButtonText(translate("Add"));
		fc.setDialogTitle(translate("Choose MP3 Directory"));
		
		while (true) {
			if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
				throw new CancelledByUser();
		
			File result = fc.getSelectedFile();
			if (!result.isDirectory()) { // User might have entered manually.
				_user.acknowledgeNotification(translate("This is not a valid folder:\n\n%1$s\n\nTry again.", result.getPath()));
				continue;
			}
			return result;
		}
	}


	class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// This method can be called only if
			// there's a valid selection
			// so go ahead and remove whatever's selected.
			int index = list.getSelectedIndex();
			listModel.remove(index);

			int size = listModel.getSize();

			if (size == 0) { // Nobody's left, disable firing.
				removeButton.setEnabled(false);

			} else { // Select an index.
				if (index == listModel.getSize()) {
					// removed item in last position
					index--;
				}

				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		}
	}

	// This listener is shared by the text field and the hire button.
	class AddListener implements ActionListener {

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			File addedDir = null;
			try {
				addedDir = chooseTargetDirectory();
			} catch (CancelledByUser e1) {
				return;
			}
			
			for (int i = listModel.getSize() -1 ; i >=0 ; i--) {
				File dir = (File)listModel.elementAt(i);
				try {
					if (isSubDirectory(dir, addedDir)) {
						return;
					}
					if (isSubDirectory(addedDir, dir)) {
						listModel.remove(i);
					}
				} catch (IOException e1) {
					return;
				}
			}  
			
			int index = list.getSelectedIndex(); // get selected index
			if (index == -1) { // no selection, so insert at beginning
				index = 0;
			} else { // add after the selected item
				index++;
			}

			listModel.insertElementAt(addedDir, index);
			// If we just wanted to add to the end, we'd do this:
			// listModel.addElement(employeeName.getText());

			// Select the new item and make it visible.
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}

		// This method tests for string equality. You could certainly
		// get more sophisticated about the algorithm. For example,
		// you might want to ignore white space and capitalization.
		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
		}
	}

	// This method is required by ListSelectionListener.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				// No selection, disable fire button.
				removeButton.setEnabled(false);

			} else {
				// Selection, enable the fire button.
				removeButton.setEnabled(true);
			}
		}
	}
	
	public File[] directories() {
		File[] directories = new File[listModel.size()];
		for (int i = 0; i < listModel.size(); i++)
			directories[i] = (File) listModel.elementAt(i);
		return  directories;
	}
	
    private boolean isSubDirectory(File base, File child) throws IOException {
	    base = base.getCanonicalFile();
	    child = child.getCanonicalFile();
	
	    File parentFile = child;
	    while (parentFile != null) {
	        if (base.equals(parentFile)) {
	            return true;
	        }
	        parentFile = parentFile.getParentFile();
	    }
	    return false;
    }

	public JButton getRemoveButton() {
		return removeButton;
	}

	public JButton getAddButton() {
		return addButton;
	}

}
