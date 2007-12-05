package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.NewContactAddition;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

class ContactsScreen extends JFrame {

	private final User _user;
	private final Party _me;
	private final ActionFactory _actionFactory;
	private final BusinessSource _businessSource;

	ContactsScreen(User user, Party me, ActionFactory actionFactory, BusinessSource businessSource) {
		_user = user;
		_me = me;
		_actionFactory = actionFactory;
		_businessSource = businessSource;
		
		initComponents();
		selectParty();
		_businessSource.output().font().addReceiver(fontReceiver());
	}
	
	private JSplitPane _splitPanel;
	private JPanel _lateral;

	private void initComponents() {
		_lateral = new JPanel();
		_lateral.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		_editPanel = new JPanel();
		_editPanel.setLayout(new BorderLayout());
		_editPanel.add(createAddButton(), BorderLayout.EAST);
		_scrollpane = new JScrollPane(createFriendsTree());
		_splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _scrollpane, _lateral);
		_splitPanel.setDividerLocation(300);
		add(_splitPanel, BorderLayout.CENTER);
		add(_editPanel, BorderLayout.SOUTH);;
		setTitle(translate("Contacts"));
		setMinimumSize(new Dimension(400,420));
	}

	private Omnivore<Font> fontReceiver() {
		return new Omnivore<Font>(){ public void consume(Font font) {
				SwingUtilities.invokeLater(new Runnable(){ public void run() {
					ContactsScreen.this.getRootPane().repaint();
					ContactsScreen.this.getRootPane().revalidate();
				}});
		}};
	}
	
	private JTree createFriendsTree() {
		final JTree tree = new JTree();
		new ContactTreeController(tree, new MeNode(_me),_businessSource.output().font());
		tree.setTransferHandler(new ContactTreeTransferHandler(_actionFactory));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				//final boolean leftClick = mouseEvent.getButton() == MouseEvent.BUTTON1;
				
				TreePath path = tree.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
				if (path==null) return;
				Object uncasted = path.getLastPathComponent();
				if (uncasted instanceof ContactNode) {
					final ContactNode node = (ContactNode)uncasted;
					if (node==null) return;
				
					selectContact(node);
					
					if (!rightClick) return;
					
					getFriendPopUpMenu(node).show(tree, mouseEvent.getX(), mouseEvent.getY());
				}else{
					selectParty();
				}
				
			}
		});

		return tree;
	}
		
	private void selectParty() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_lateral.removeAll();
				_lateral.add(new MePanel(_user, _businessSource));
				_lateral.revalidate();
				_lateral.repaint();
			}
		});
	}

	private void selectContact(final ContactNode node) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				_lateral.removeAll();
				_lateral.add(new ContactPanel(_user, node.contact(),_businessSource.contactNickChanger(),_businessSource.contactMsnAddressChanger(),_businessSource.output().font()));
				_lateral.revalidate();
				_lateral.repaint();
			}
		});
	}
	

	private JPopupMenu getFriendPopUpMenu(final ContactNode node) {
		final JPopupMenu result = new JPopupMenu();
		for (ContactAction action : _actionFactory.contactActions()) addToContactMenu(result, action, node);
		addToContactMenu(result, new ContactRemovalAction(_businessSource.contactRemover()), node);
		return result;
	}

	private void addToContactMenu(JPopupMenu menu, final ContactAction action, final ContactNode node) {
		final JMenuItem item = new JMenuItem(action.caption());
		item.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ignored) {
			Threads.startDaemon(new Runnable() { @Override public void run() {
				action.actUpon(node.contact());
			}});
		}});
		
		menu.add(item);
	}

	private JButton createAddButton() {
		JButton addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ignored) {
				try {
					new NewContactAddition(_user, _businessSource.contactAdder());
				} catch (CancelledByUser cbu) {
					//Fair enough.
				}
			}

		});
		return addButton;
	}
	
	private static final long serialVersionUID = 1L;
	private JScrollPane _scrollpane;
	private JPanel _editPanel;
	
}