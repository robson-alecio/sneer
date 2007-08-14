package sneer.kernel.appmanager.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static wheel.i18n.Language.*;

import sneer.kernel.appmanager.AppManager;
import wheel.io.Log;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
		
		final JButton installButton = new JButton(translate("Install"));
		installButton.setMaximumSize(new Dimension(100, 30));
		installButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		installButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(translate("Choose App .src.jar file"));
				int value = fc.showOpenDialog(null);
				if (value != JFileChooser.APPROVE_OPTION) return;
				File jarFile = fc.getSelectedFile();
				String appName = null;
				while(appName == null)
					appName = JOptionPane.showInputDialog(translate("Please choose the App Name")); //Implement: use the manifest file to store the app name and version
				try{
					_appManager.install(appName, jarFile);
				}catch(IOException ioe){
					Log.log(ioe);
					ioe.printStackTrace();
				}
				updateList();
			}
		});
		final JButton removeButton = new JButton(translate("Remove"));
		removeButton.setMaximumSize(new Dimension(100, 30));
		removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String appName=(String)_appList.getSelectedValue();
				if (appName==null)
					return;
				_appManager.remove(appName);
				updateList();
			}
		});
		removeButton.setEnabled(false);
		
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
		buttonPanel.add(installButton);
		buttonPanel.add(removeButton);
		add(new JScrollPane(_appList),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		setSize(300,400);
		setVisible(true);
	}

	private void updateList() { //Implement: this should be a reactive model
		_listModel.clear(); 
		for(String temp:_appManager.installedApps().keySet())
			_listModel.add(0,temp);
	}
	
	private static final long serialVersionUID = 1L;
	

}
