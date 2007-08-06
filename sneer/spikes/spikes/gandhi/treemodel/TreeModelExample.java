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

import sneer.kernel.gui.contacts.ContactCellRenderer;

public class TreeModelExample extends JFrame {

	final static ImageIcon ICON = new ImageIcon(ContactCellRenderer.class.getResource("/spikes/gandhi/treemodel/icon.gif"));
	
	DefaultTreeModel model;
	
	public TreeModelExample() {
		super();
		model = new DefaultTreeModel(new MyTreeNode("root"));

		model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), (MyTreeNode) model.getRoot(), 0); //init
		model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), (MyTreeNode) model.getRoot(), 0);
		model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), (MyTreeNode) model.getRoot(), 0);

		final JTree tree = new JTree(model);
		
		tree.setShowsRootHandles(true);
		tree.addTreeExpansionListener(new TreeExpansionListener(){
			
			public void treeCollapsed(TreeExpansionEvent event) {
				final MyTreeNode node = (MyTreeNode) event.getPath().getLastPathComponent();
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
				final MyTreeNode node = (MyTreeNode) event.getPath().getLastPathComponent();
				System.out.println("expand " + node);
				if (node == null) return;
				if (node.getParent() == null) return; //ignore root

				model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), node, 0);
				model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), node, 0);
				model.insertNodeInto(new MyTreeNode(new Date()+" - "+gen()), node, 0);

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
	}
	
	private void removeRecursive(MyTreeNode node) {
		for(int t=model.getChildCount(node)-1;t>=0;t--){  // the nodes MUST be removed backwards, because its based on index position! argh..
			MyTreeNode child = (MyTreeNode) model.getChild(node,t);
			removeRecursive(child);
		}
		System.out.println("removing " + node);
		model.removeNodeFromParent(node);
	}

	private void removeChildrenRecursive(MyTreeNode node) {
		System.out.println("removing children ----");
		for(int t=model.getChildCount(node)-1;t>=0;t--){ // the nodes MUST be removed backwards, because its based on index position! argh...
			MyTreeNode child = (MyTreeNode) model.getChild(node,t);
			removeRecursive(child);
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
