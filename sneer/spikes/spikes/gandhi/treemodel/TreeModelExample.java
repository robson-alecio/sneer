package spikes.gandhi.treemodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class TreeModelExample extends JFrame{


	public TreeModelExample() {
		super(); 
		final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
		final JTree tree = new JTree(model);
		
		tree.setShowsRootHandles(true);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (!rightClick) return;
				TreePath path = tree.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
				
				final JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem addItem = new JMenuItem("Add");
				addItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						model.insertNodeInto(new DefaultMutableTreeNode(new Date()), node, 0);
					}	
				});
				JMenuItem removeItem = new JMenuItem("Remove");
				removeItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						model.removeNodeFromParent(node);
					}	
				});
				popupMenu.add(addItem);
				popupMenu.add(removeItem);
				popupMenu.show(tree, mouseEvent.getX(), mouseEvent.getY());
				
			}
		});
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(new JScrollPane(tree));
	    getContentPane().add(main);
	    main.setPreferredSize(new Dimension(500,500));
	    
	    pack(); 
	    addWindowListener(new WindowAdapter(){ 
	        @Override
			public void windowClosing(WindowEvent evt){ 
	           System.exit(0);
	         } 
	    }); 
	    setVisible(true); 
	}
	
	public static void main(String[] args){
		new TreeModelExample();
	}
	
	private static final long serialVersionUID = 1L;
}
