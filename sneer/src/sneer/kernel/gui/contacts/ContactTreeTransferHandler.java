package sneer.kernel.gui.contacts;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import sneer.kernel.pointofview.Contact;
import wheel.lang.Threads;

public class ContactTreeTransferHandler extends TransferHandler {

	private final DropActionFactory _dropActionFactory;

	public ContactTreeTransferHandler(DropActionFactory dropActionFactory){
		_dropActionFactory = dropActionFactory;
	}
	
	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) || !support.isDrop())
			return false;
		JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
		return dropLocation.getPath() != null;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support))
			return false;

		JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
		TreePath path = dropLocation.getPath();
		Transferable transferable = support.getTransferable();
		String transferData;
		try {
			transferData = (String) transferable.getTransferData(DataFlavor.stringFlavor);
		} catch (IOException e) {
			return false;
		} catch (UnsupportedFlavorException e) {
			return false;
		}
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		
		if ((node==null)||(!(node instanceof ContactNode)))
			return true;
		
		Contact contact = ((ContactNode)node).contact();
		
		System.out.println(transferData);
		
		Map<Object, List<DropAction>> dropActionsInterestedInObject = new Hashtable<Object, List<DropAction>>();
		
		//Fix: Currently only supports File dropping...
		if (!transferData.startsWith("file:"))
			return true;
		
		List<Object> objects = new ArrayList<Object>();
		BufferedReader reader= new BufferedReader(new StringReader(transferData)); 
		String line = null;
		try{
			while((line = reader.readLine())!=null)
				objects.add(convertToFile(line));
		}catch(IOException ignored){
		}
		for(Object object:objects){
			for(DropAction dropAction:_dropActionFactory.dropActions())
				if (dropAction.interested(object))				
					produceDropActionList(dropActionsInterestedInObject, object).add(dropAction);
		}
		
		for(Object object:objects){
			List<DropAction> interestedDropActions = dropActionsInterestedInObject.get(object);
			if (interestedDropActions.isEmpty())
				continue;
			if (interestedDropActions.size()==1){
				interestedDropActions.get(0).actUpon(contact, object);
				continue;
			}
			showAppDropChooser(interestedDropActions, contact, object);
		}
		
		return true;
	}
	
	private List<DropAction> produceDropActionList(Map<Object, List<DropAction>> dropActionsInterestedInObject, Object object) {
		List<DropAction> interestedList = dropActionsInterestedInObject.get(object);
		if (interestedList==null){
			interestedList = new ArrayList<DropAction>();
			dropActionsInterestedInObject.put(object, interestedList);
		}
		return interestedList;
	}

	private File convertToFile(String transferData) {
		File file = null;
		URL url = null;
		try {
			url = new URL(transferData);
		} catch (MalformedURLException e1) {
			return null;
		}
		try {
			file = new File(url.toURI());
		} catch(URISyntaxException e) {
			file = new File(url.getPath());
		}
		return file;
	}

	private void showAppDropChooser(final List<DropAction> interested, final Contact contact, final Object object) {
		Threads.startDaemon(new Runnable(){ public void run() {
				new DropChooser(interested, contact, object);
		}});
	}

	private static final long serialVersionUID = 1L;

}
