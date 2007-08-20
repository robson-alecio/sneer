package sneer.kernel.appmanager.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.appmanager.SovereignApplicationUID;
import wheel.io.Log;

public class AppManagerGui extends JFrame{

	private JList _appList;
	private DefaultListModel _listModel = new DefaultListModel();
	private final AppManager _appManager;
	
	public AppManagerGui(AppManager appManager){
		_appManager = appManager;
		setLayout(new BorderLayout());
		setTitle(translate("App Manager"));
		_appList = new JList(_listModel);
		updateList();

		final JButton removeButton = new JButton(translate("Remove"));
		removeButton.setMaximumSize(new Dimension(100, 30));
		removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String appName=(String)_appList.getSelectedValue();
				if (appName==null)
					return;
				_appManager.removeAppDirectories(appName);
				updateList();
			}
		});
		removeButton.setEnabled(false);
		
		final JButton publishButton = new JButton(translate("Publish"));
		publishButton.setMaximumSize(new Dimension(100, 30));
		publishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		publishButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle(translate("Choose the App Sources folder"));
				int value = fc.showOpenDialog(null);
				if (value != JFileChooser.APPROVE_OPTION) return;
				File baseFolder = fc.getSelectedFile();
				
				if (baseFolder==null)
					return;
				
				File srcFolder = new File(baseFolder,"src");
				
				File application = AppTools.findApplicationSource(srcFolder);

				if (application==null)
					throw new RuntimeException("CANT PUBLISH THIS! No Application.java!"); //FixUrgent: use proper message

				try{
					_appManager.publish(baseFolder);
				}catch(Exception ioe){
					Log.log(ioe);
					ioe.printStackTrace();
				}
				updateList();
			}

		});
		
		_appList.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				String value = (String) _appList.getSelectedValue();
				if (value == null){
					removeButton.setEnabled(false);
				}else{
					removeButton.setEnabled(true);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		buttonPanel.add(publishButton);
		buttonPanel.add(removeButton);
		add(new JScrollPane(_appList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		setSize(300,400);
		setVisible(true);
	}

	private void updateList() { //Implement: this should be a reactive model
		_listModel.clear(); 
		for(SovereignApplicationUID temp:_appManager.publishedApps().output())
			_listModel.add(0,temp._appName);
	}
	
	private static final long serialVersionUID = 1L;
	

}
