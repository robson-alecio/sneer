package sneer.apps.publicfiles.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import sneer.apps.publicfiles.packet.PleaseSendFile;
import sneer.apps.transferqueue.TransferKey;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.files.impl.FileInfo;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class PublicFilesFrame extends JFrame{

	private final Signal<String> _otherGuyNick;
	private final Signal<FileInfo[]> _fileInfos;
	private final User _user;
	private final ContactId _contactId;
	private final Channel _channel;
	private final TransferQueue _transfer;

	public PublicFilesFrame(Channel channel, TransferQueue transfer, User user, ContactId contactId, Signal<String> otherGuyNick, Signal<FileInfo[]> fileInfos){
		_channel = channel;
		_transfer = transfer;
		_user = user;
		_contactId = contactId;
		_otherGuyNick = otherGuyNick;
		_fileInfos = fileInfos;
		initComponents();
		addReceivers();
	}
	
	private JList _fileList;
	private DefaultListModel _listModel = new DefaultListModel();
	private JButton _saveAsButton;
	private JButton _copyToClipboard;
	private JProgressBar _progress = new JProgressBar();
	
	private void initComponents() {
		setLayout(new BorderLayout());
		_fileList = new JList(_listModel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		_saveAsButton = new JButton(translate("Save in Folder"));
		_saveAsButton.setMaximumSize(new Dimension(150, 30));
		_saveAsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_saveAsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestFiles(toFileInfos(_fileList.getSelectedValues()));
			}
		});
		buttonPanel.add(_saveAsButton);
		_copyToClipboard = new JButton(translate("To ClipBoard"));
		_copyToClipboard.setMaximumSize(new Dimension(150, 30));
		_copyToClipboard.setAlignmentX(Component.CENTER_ALIGNMENT);
		_copyToClipboard.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				toClipboard(toFileInfos(_fileList.getSelectedValues()));
			}
		});
		buttonPanel.add(_copyToClipboard);
		_progress.setStringPainted(true);
		_progress.setValue(0);
		add(new JScrollPane(_fileList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		add(_progress,BorderLayout.SOUTH);
		setSize(300,400);
		setVisible(true);
	}
	
	private void toClipboard(FileInfo[] infos) {
		_copyToClipboard.setEnabled(false);
		File directory = AppTools.createTempDirectory("clipboard");
		callback(infos).consume(directory);
		copyToClipboardOnTranferEnd(directory);
	}
	
	//Fix: _transferLock should have some kind of timeout if other side stops sending
	//Fix: how to use a Lock within different threads?
	private Boolean _transferLock = false;

	private void copyToClipboardOnTranferEnd(final File directory) {
		Threads.startDaemon(new Runnable(){ public void run() {
			waitForLock();
			_transferLock=true;
			System.out.println("copying to clipboard...");
			File[] files = directory.listFiles();
			for(File file:files)
				System.out.println("selected:"+file.getAbsolutePath());
			FileSelection sel = new FileSelection(Arrays.asList(files));		
			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
			//AppTools.removeRecursive(directory);
			_copyToClipboard.setEnabled(true);
			_transferLock=false;
		}});
	}
	
	private void waitForLock(){
		while(_transferLock)
			Threads.sleepWithoutInterruptions(1000);
	}

	private FileInfo[] toFileInfos(Object[] values){
		FileInfo[] result = new FileInfo[values.length];
		for(int t=0;t<values.length;t++)
			result[t]=(FileInfo)values[t];
		return result;
	}

	private void requestFiles(FileInfo[] infos) {
		_user.chooseDirectory(translate("Choose Directory"), translate("Save Inside"), callback(infos));
	}
	
	private Map<String,Long> _currentTransfers = new Hashtable<String,Long>();
	
	private Omnivore<File> callback(final FileInfo[] infos) {
		return new Omnivore<File>(){ public void consume(File directory) {
			if (directory == null)
				return;
			_transferLock=true;
			long total = 0;
			for(FileInfo info:infos)
				total+=info._size;
			_currentTransfers.clear();
			for(FileInfo info:infos){
				String transferId = AppTools.uniqueName("transferId"); //Refactor: unify multi purpose random 
				File target = new File(directory,info._name);
				if (target.getParentFile()!=null)
					target.getParentFile().mkdirs();
				_transfer.receiveFile(new TransferKey(transferId,_contactId), target.getAbsolutePath(), info._size, progressCallback(transferId,total));
				_channel.output().consume(new Packet(_contactId,new PleaseSendFile(transferId,info)));
			}
		}};
	}
	
	private Omnivore<Long> progressCallback(final String transferId, final long total) {
		return new Omnivore<Long>(){ 
			public void consume(Long value) {
				_currentTransfers.put(transferId, value);
				long current=0;
				for(Long item:_currentTransfers.values())
					current+=item;
			updateProgressBar(current, total);
			if (current==total) 
				_transferLock=false;
		}};
	}

	public void updateProgressBar(final long value, final long total){
		SwingUtilities.invokeLater(new Runnable(){ public void run() {
			int current = (int)((value*100)/total);
			_progress.setValue(current);
		}});
	}
	
	private void addReceivers() {
		_otherGuyNick.addReceiver(nickChangeReceiver());
		_fileInfos.addReceiver(fileInfosChangeReceiver());
	}
	
	private Omnivore<String> nickChangeReceiver() {
		return new Omnivore<String>(){ public void consume(final String nick) {
			SwingUtilities.invokeLater(new Runnable(){ public void run() {
				setTitle(translate("Public Files for %1$s",nick));
			}});
		}};
	}
	
	private Omnivore<FileInfo[]> fileInfosChangeReceiver() {
		return new Omnivore<FileInfo[]>(){ public void consume(final FileInfo[] fileInfos) {
			SwingUtilities.invokeLater(new Runnable(){ public void run() {
				_listModel.clear();
				Arrays.sort(fileInfos);
				for(FileInfo info:fileInfos)
					_listModel.add(0, info);
				_fileList.revalidate();
				_fileList.repaint();
			}});
		}};
	}
	
	private static final long serialVersionUID = 1L;

	//Fix: paste not working on gnome (nautilus) ...  :(
	public static class FileSelection implements Transferable {
		private final String URI_LIST = "uri-list";
		
        private List<File> _files;
    
        public FileSelection(List<File> files) {
        	_files = files;
        }
    
        public DataFlavor[] getTransferDataFlavors() {
            return _flavors;
        }
    
        @SuppressWarnings("deprecation")
		public boolean isDataFlavorSupported(DataFlavor flavor) {
        	System.out.println("detect Flavor: "+ flavor.getHumanPresentableName());

        	// windows
			if (flavor.equals(DataFlavor.javaFileListFlavor))
				return true;
			// kde
			if (URI_LIST.equals( flavor.getSubType() )) 			
				return true;
			// gnome
			//if (flavor.equals(_gnomeFilesFlavor))
			//	return true;
			return false;
        }
    
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException{

        	if (flavor.equals(DataFlavor.javaFileListFlavor)) 
                return _files;
        	if (flavor.equals(_uriListFlavor)) {
        		String data = "";
                for(File file:_files)
                	data+= file.toURI() + "\n";
                return data;
            }
        	/*if (flavor.equals(_gnomeFilesFlavor)) {
            	String data="copy";
                for(File file:_files)
                	data+= file.toURI() + "\r\n";
                return data;
            }*/
            throw new UnsupportedFlavorException(flavor);
        }
    }
	
	private static DataFlavor _uriListFlavor;
	//private static DataFlavor _gnomeFilesFlavor;
	static {
	     try {
	    	 _uriListFlavor = new  DataFlavor("text/uri-list;class=java.lang.String");
	    	 //_gnomeFilesFlavor = new DataFlavor("x-special/gnome-copied-files;class=java.lang.String");
	     } catch (ClassNotFoundException e) { // can't happen
	         e.printStackTrace();
	     }
	}
	@SuppressWarnings("deprecation")
	private static DataFlavor[] _flavors = new DataFlavor[]{DataFlavor.javaFileListFlavor,_uriListFlavor};
}
