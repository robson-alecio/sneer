package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultMutableTreeNode;
import static wheel.i18n.Language.*;

public class NoContactNode extends DefaultMutableTreeNode{

	public NoContactNode(){
		super(translate("No contacts"));
	}
	
	private static final long serialVersionUID = 1L;
	
}
