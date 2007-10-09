package sneer.apps.publicfiles.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

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
	private JProgressBar _progress = new JProgressBar();
	
	private void initComponents() {
		setLayout(new BorderLayout());
		_fileList = new JList(_listModel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		_saveAsButton = new JButton(translate("Request it"));
		_saveAsButton.setMaximumSize(new Dimension(100, 30));
		_saveAsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_saveAsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestFiles(toFileInfos(_fileList.getSelectedValues()));
			}
		});
		buttonPanel.add(_saveAsButton);
		_progress.setStringPainted(true);
		_progress.setValue(0);
		add(new JScrollPane(_fileList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		add(_progress,BorderLayout.SOUTH);
		setSize(300,400);
		setVisible(true);
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

	private Omnivore<File> callback(final FileInfo[] infos) {
		return new Omnivore<File>(){ public void consume(File directory) {
			long total = 0;
			for(FileInfo info:infos)
				total+=info._size;
			Omnivore<Long> progressCallback = progressCallback(total);
			for(FileInfo info:infos){
				String transferId = AppTools.uniqueName("transferId"); //Refactor: unify multi purpose random 
				File target = new File(directory,info._name);
				if (target.getParentFile()!=null)
					target.getParentFile().mkdirs();
				_transfer.receiveFile(new TransferKey(transferId,_contactId), target.getAbsolutePath(), info._size, progressCallback);
				_channel.output().consume(new Packet(_contactId,new PleaseSendFile(transferId,info)));
			}
		}};
	}

	//FixUrgent: totally broken progressbar for multiple files, should keep last progress for each transfer.
	private Omnivore<Long> progressCallback(final long total) {
		return new Omnivore<Long>(){ 
			public void consume(Long value) {
			updateProgressBar(value, total);
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

}
