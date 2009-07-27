package sneer.bricks.softwaresharing.gui.impl;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import sneer.bricks.softwaresharing.BrickVersion;

class MeTooTreeCellRenderer extends DefaultTreeCellRenderer {

@Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
			boolean expanded, boolean leaf, int row, boolean hasFocus_) {
	  
	JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus_);
    
    ImageIcon icon = ((AbstractTreeNodeWrapper<?>) value).getIcon();
    
    if (icon != null) 
    	result.setIcon(icon);

    if(value instanceof BrickInfoTreeNode)
    	if(((BrickInfoTreeNode)value).isBrickStagedForExecution())
			isStaged(result);
    
    if(value instanceof BrickVersionTreeNode)
    	if(((BrickVersion)((BrickVersionTreeNode)value).sourceObject()).isStagedForExecution())
			isStaged(result);
    
    return result;
  }

	private void isStaged(JLabel result) {
		result.setForeground(Color.BLUE);
	}
}