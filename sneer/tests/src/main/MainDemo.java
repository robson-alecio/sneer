package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import wheel.io.Logger;
import wheel.lang.Environments;

public class MainDemo {

	private static final String USER_INFO_PROPERTIES_FILE = "user.info.properties";
	private static final String YOUR_OWN_NAME = "yourOwnName";
	private static final String DYN_DNS_USER = "dynDnsUser";
	private static final String DNY_DNS_PASSWORD = "dnyDnsPassword";
	private static Container _container;

	public static void main(String[] args) {
		try {
			try {
				tryToRun(args);
			} catch (ArrayIndexOutOfBoundsException e) {
				tryRememberArguments();
			} 
		} catch (Exception e) {
			e.printStackTrace();
			exitWithUsageMessage();
		}		
	}
	
	private static void exitWithUsageMessage() {
		System.err.println("\nUsage: MainDemo [Dummy]\n");
		System.exit(1);
	}

	private static void tryRememberArguments() throws Exception{
		Properties userInfo = new Properties();
		File file = new File(USER_INFO_PROPERTIES_FILE);
		System.out.println("read from  " + file.getAbsolutePath() );
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			userInfo.load(in);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			askToUser(userInfo);
			try { in.close(); } catch (Exception e) {/*ignore*/}
		}
	}

	private static void askToUser(Properties userInfo) {
		new WelcomeWizard(userInfo);
	}

	private static void tryToRun(final String[] args) throws Exception {
		Logger.redirectTo(System.out);

		checkForDummy(args);
		
		Environments.runWith(container(), new Runnable() { @Override public void run() {
			demo().start(ownName(args), dynDnsUser(args), dynDnsPassword(args));			
		}});
	}

	private static void checkForDummy(String[] args) {
		if (!"Dummy".equals(ownName(args))) return;
		
		File dummyHome = new File(System.getProperty("user.home"), ".sneerdummy");
		System.setProperty("home_override", dummyHome.getAbsolutePath());
	}

	private static Container container() {
		if (_container == null) _container = Containers.newContainer();
		return _container;
	}


	private static MainDemoBrick demo() {
		return container().provide(MainDemoBrick.class);
	}
	
	private static String ownName(String[] args) {
		return args[0];
	}

	private static String dynDnsUser(String[] args) {
		if (args.length < 2) return null;
		return args[1];
	}

	private static String dynDnsPassword(String[] args) {
		if (args.length < 2) return null;
		return args[2];
	}
	
	private static class WelcomeWizard extends JDialog {
		
		private final Properties _userInfo;
		
		private final JTextField _yourOwnName = new JTextField();
		private final JTextField _dynDnsUser = new JTextField();
		private final JTextField _dnyDnsPassword = new JTextField();

		private WelcomeWizard(Properties userInfo) {
			_userInfo = userInfo;
			initGui();
		}

		private void initGui() {
			setBounds(10, 10, 300, 200);
			java.awt.Container pnl = getContentPane();
			setTitle("Welcome, please inform your data:");
			
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			
			_yourOwnName.setText(_userInfo.getProperty(YOUR_OWN_NAME));
			_dynDnsUser.setText(_userInfo.getProperty(DYN_DNS_USER));
			_dnyDnsPassword.setText(_userInfo.getProperty(DNY_DNS_PASSWORD));

			pnl.setLayout(new GridLayout(4,1));
			pnl.add(_yourOwnName);
			pnl.add(_dynDnsUser);
			pnl.add(_dnyDnsPassword);
			
			_yourOwnName.setBorder(new TitledBorder(YOUR_OWN_NAME));
			_dynDnsUser.setBorder(new TitledBorder(DYN_DNS_USER  + " [optional]"));
			_dnyDnsPassword.setBorder(new TitledBorder(DNY_DNS_PASSWORD + " [optional]"));
			
			final JButton btn = new JButton("Start My Sneer!");
			pnl.add(btn);
			
			btn.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
				try {
					submitUserInformation();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(btn, ex.getMessage());
					ex.printStackTrace();
					exitWithUsageMessage();
				}						
			}});
			setVisible(true);
		}

		private void submitUserInformation() throws Exception {
			setVisible(false);
			storeProperties();
			String args[] = new String[_userInfo.size()];
			args[0] = _userInfo.getProperty(YOUR_OWN_NAME);

			if(_userInfo.size()==1) {
				tryToRun(args);
				return;
			}
			
			args[1] = _userInfo.getProperty(DYN_DNS_USER);
			args[2] = _userInfo.getProperty(DNY_DNS_PASSWORD);
			tryToRun(args);
		}

		private void storeProperties() {
			_userInfo.setProperty(YOUR_OWN_NAME, _yourOwnName.getText());	
			String password = _dnyDnsPassword.getText().trim();
			String user = _dynDnsUser.getText().trim();
			if(password.length()>0 && user.length()>0){
				_userInfo.setProperty(DYN_DNS_USER, user);
				_userInfo.setProperty(DNY_DNS_PASSWORD, password);
			}
			FileOutputStream out = null;
			try {
				File file = new File(USER_INFO_PROPERTIES_FILE);
				System.out.println("write to " + file.getAbsolutePath());
				out = new FileOutputStream(file);
				_userInfo.store(out, new Date().toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try { out.close(); } catch (Exception e) {/*ignore*/}
			}
		}
	}
}