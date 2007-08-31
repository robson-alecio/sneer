package sneer.apps.metoo.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import sneer.apps.metoo.MeTooPacket;
import sneer.apps.metoo.packet.AppFilePart;
import sneer.apps.metoo.packet.AppInstallRequest;
import sneer.apps.metoo.packet.AppListRequest;
import sneer.apps.metoo.packet.AppListResponse;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class MeTooFrame extends JFrame{
	
	private final Channel _channel;
	private final Contact _contact;
	private final Signal<MeTooPacket> _input;

	private JList _appList;
	private DefaultListModel _listModel = new DefaultListModel();
	private Map<String, String> _installNameAndAppUID = new HashMap<String, String>();
	private final File _tempDirectory;
	private final AppManager _appManager;
	private final User _user;

	public MeTooFrame(User user, Channel channel, Contact contact, Signal<MeTooPacket> input, File tempDirectory, AppManager appManager){
		_user = user;
		_channel = channel;
		_contact = contact;
		_input = input;
		_tempDirectory = tempDirectory;
		_appManager = appManager;
		_input.addReceiver(meTooPacketReceiver());
		initComponents();
		sendAppListRequest();
		
	}
	
	private JProgressBar _progress = new JProgressBar();
	
	private void initComponents() {
		setLayout(new BorderLayout());
		setTitle(translate("Available Applications"));
		_appList = new JList(_listModel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		JButton installButton = new JButton(translate("Me Too"));
		installButton.setMaximumSize(new Dimension(100, 30));
		installButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		installButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String installName=(String)_appList.getSelectedValue();
				if (installName==null)
					return;
				sendAppInstallRequest(installName);
			}
		});
		buttonPanel.add(installButton);
		_progress.setStringPainted(true);
		_progress.setValue(0);
		add(new JScrollPane(_appList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		add(_progress,BorderLayout.SOUTH);
		setSize(300,400);
		setVisible(true);
	}
	
	private void sendAppInstallRequest(String installName) {
		System.out.println("sending install request: "+ installName);
		_channel.output().consume(new Packet(_contact.id(),new AppInstallRequest(installName)));
	}

	private Omnivore<MeTooPacket> meTooPacketReceiver() {
		return new Omnivore<MeTooPacket>(){
			public void consume(MeTooPacket meTooPacket) {
				switch(meTooPacket.type()){
				case MeTooPacket.APP_LIST_RESPONSE:
					updateAppList(((AppListResponse)meTooPacket)._installNameAndAppUID);
					break;
				case MeTooPacket.APP_FILE_PART:
					receiveAppFile((AppFilePart)meTooPacket);
					break;
				}
			}
		};
	}
	
	protected void receiveAppFile(AppFilePart appFilePart) {
		File file = new File(_tempDirectory, appFilePart._filename);
		updateProgressBar(appFilePart._offset,appFilePart._filesize);
		try {
			RandomAccessFile raf = new RandomAccessFile(file,"rws"); //Refactor Use a regular file, not RandomAccessFile. Use wheel.io.files.Directory.
			raf.seek(appFilePart._offset);
			raf.write(appFilePart._content,0,appFilePart._content.length);
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); //Fix: Treat properly
		} catch (IOException e) {
			e.printStackTrace(); //Fix: Treat properly
		}
		updateProgressBar(appFilePart._offset+appFilePart._content.length,appFilePart._filesize);
		if ((appFilePart._offset+appFilePart._content.length)>=appFilePart._filesize){ //file ended
			String installName = _appManager.publishFromZipFile(file);
			file.delete();
			sendAppListRequest();
			_user.acknowledgeNotification(translate("Application successfully installed: \n\n %1$s",installName.substring(0,installName.indexOf("-"))));
		}
	}
	
	protected void updateAppList(Map<String, String> installNameAndAppUID) {
		_installNameAndAppUID = installNameAndAppUID;
		_listModel.clear();
		for(String installName:_installNameAndAppUID.keySet())
			_listModel.add(0, installName);
	}
	
	public void updateProgressBar(long value, long total){
		int current = (int)((value*100)/total);
		_progress.setValue(current);
	}

	public void sendAppListRequest(){
		_channel.output().consume(new Packet(_contact.id(),new AppListRequest()));
	}

	private static final long serialVersionUID = 1L;

}
