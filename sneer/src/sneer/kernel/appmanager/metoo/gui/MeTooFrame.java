package sneer.kernel.appmanager.metoo.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.SwingUtilities;

import sneer.apps.transferqueue.TransferKey;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.appmanager.metoo.packet.AppInstallRequest;
import sneer.kernel.appmanager.metoo.packet.AppListRequest;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import wheel.io.files.impl.WindowsAndLinuxCompatibility;
import wheel.io.ui.User;
import wheel.lang.Omnivore;

public class MeTooFrame extends JFrame{
	
	private final Channel _channel;
	private final Contact _contact;

	private JList _appList;
	private DefaultListModel _listModel = new DefaultListModel();
	private Map<String, Long> _installNameAndSize = new HashMap<String, Long>();
	private final AppManager _appManager;
	private final User _user;
	private final TransferQueue _transfer;
	private final File _tempDirectory;
	
	public MeTooFrame(User user, Channel channel, Contact contact, AppManager appManager, TransferQueue transfer){
		_user = user;
		_channel = channel;
		_contact = contact;
		_appManager = appManager;
		_transfer = transfer;
		_tempDirectory = AppTools.createTempDirectory("metoo");
		initComponents();
		sendAppListRequest();
	}
	
	private JProgressBar _progress = new JProgressBar();
	private JButton _installButton; 
	
	private void initComponents() {
		setLayout(new BorderLayout());
		setTitle(translate("Available Applications"));
		_appList = new JList(_listModel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		_installButton = new JButton(translate("Me Too"));
		_installButton.setMaximumSize(new Dimension(100, 30));
		_installButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_installButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String installName=(String)_appList.getSelectedValue();
				if (installName==null)
					return;
				sendAppInstallRequest(installName);
				_installButton.setEnabled(false);
			}
		});
		buttonPanel.add(_installButton);
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
		TransferKey key = new TransferKey(installName,_contact.id());
		File target = new File(_tempDirectory,installName);
		target = WindowsAndLinuxCompatibility.normalizedFile(target.getAbsolutePath());
		long total = _installNameAndSize.get(installName);
		_transfer.receiveFile(key, target.getAbsolutePath(), total, receiverProgressCallback(target, installName,total));
		_channel.output().consume(new Packet(_contact.id(),new AppInstallRequest(installName)));
	}

	private Omnivore<Long> receiverProgressCallback(final File target, final String installName, final long total) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			System.out.println("Receiving :"+installName+" - "+ value);
			if (value == total){
				_appManager.publishFromZipFile(target);
				_installButton.setEnabled(true);
				_user.modelessAcknowledge(translate("Information"), translate("Application successfully installed: \n\n %1$s",installName.substring(0,installName.indexOf("-"))));
				sendAppListRequest();
			}
			updateProgressBar(value,total);
		}};
	}
	
	public void updateAppList(final Map<String, Long> installNameAndSize) {
		SwingUtilities.invokeLater(new Runnable(){ public void run() {
			_installNameAndSize = installNameAndSize;
			_listModel.clear();
			for(String installName:_installNameAndSize.keySet())
				_listModel.add(0, installName);
		}});
	}
	
	public void updateProgressBar(final long value, final long total){
		SwingUtilities.invokeLater(new Runnable(){ public void run() {
			int current = (int)((value*100)/total);
			_progress.setValue(current);
		}});
	}

	public void sendAppListRequest(){
		_channel.output().consume(new Packet(_contact.id(),new AppListRequest()));
	}

	private static final long serialVersionUID = 1L;

}
