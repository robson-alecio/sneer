package spikes.gandhi.treemodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import static sneer.foundation.environments.Environments.my;

public class TreeModelExample extends JFrame {

	final static ImageIcon ICON = new ImageIcon(TreeModelExample.class.getResource("/spikes/gandhi/treemodel/icon.gif"));
	
	final DefaultTreeModel _model;
	
	public TreeModelExample() {
		super();
		_model = new DefaultTreeModel(new NonLeafNode("root"));

		_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), (NonLeafNode) _model.getRoot(), 0); //init
		_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), (NonLeafNode) _model.getRoot(), 0);
		_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), (NonLeafNode) _model.getRoot(), 0);

		final JTree tree = new JTree(_model);
		
		tree.setShowsRootHandles(true);
		tree.addTreeExpansionListener(new TreeExpansionListener(){
			
			public void treeCollapsed(TreeExpansionEvent event) {
				final NonLeafNode node = (NonLeafNode) event.getPath().getLastPathComponent();
				System.out.println("collapse " + node);
				if (node == null) return;
				if (node.getParent() == null) return; //ignore root

				removeChildrenRecursive(node);
			}

			public void treeExpanded(TreeExpansionEvent event) {
				//ignored
			}
		});

		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillCollapse(TreeExpansionEvent event) {
				//ignored
			}

			public void treeWillExpand(TreeExpansionEvent event) {
				final NonLeafNode node = (NonLeafNode) event.getPath().getLastPathComponent();
				System.out.println("expand " + node);
				if (node == null) return;
				if (node.getParent() == null) return; //ignore root

				_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), node, 0);
				_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), node, 0);
				_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), node, 0);

			}
		});

		JPanel main = new JPanel(new BorderLayout());
		main.add(new JScrollPane(tree));
		getContentPane().add(main);
		main.setPreferredSize(new Dimension(500, 500));

		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});
		setVisible(true);
		
		startConcurrentModelModifiers();
	}
	
	private void startConcurrentModelModifiers() {
		my(Threads.class).startDaemon("Tree Model Node Adder", nodeAdder());
		my(Threads.class).startDaemon("Tree Model Node Remover", nodeRemover());
	}

	private Runnable nodeAdder() {
		return new Runnable() { @Override public void run() {
			while (true) {
				try {
					my(GuiThread.class).invokeAndWait(new Runnable(){@Override public void run() {
						addNode();
					}});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}};
	}

	private Runnable nodeRemover() {
		return new Runnable() { @Override public void run() {
			while (true) {
				try {
					my(GuiThread.class).invokeAndWait(new Runnable(){@Override public void run() {
						removeNode();
					}});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		}};
	}

	synchronized private void removeNode() {
		if (_model.getChildCount(_model.getRoot()) == 0) return;
		_model.removeNodeFromParent((MutableTreeNode) _model.getChild(_model.getRoot(), 0));
	}

	synchronized private void addNode() {
		if (_model.getChildCount(_model.getRoot()) == 5) return;
		_model.insertNodeInto(new NonLeafNode(new Date()+" - "+gen()), (NonLeafNode) _model.getRoot(), 0);
	}

	private void removeChildrenRecursive(Object node) {
		System.out.println("removing children ----");
		while (_model.getChildCount(node) != 0) {
			Object child = _model.getChild(node, 0);
			removeChildrenRecursive(child);
			_model.removeNodeFromParent((MutableTreeNode)child);
		}
		System.out.println("children removed  ----");
	}

	public static void main(String[] args) {
		UIManager.put("Tree.collapsedIcon",ICON); //affe... olha onde seta...
        UIManager.put("Tree.expandedIcon",ICON);
		new TreeModelExample();
	}

	private static final long serialVersionUID = 1L;
	
	private static long _generated =0;
	
	private synchronized long gen(){
		return _generated++;
	}
}
