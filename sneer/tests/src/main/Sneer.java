package main;

import static sneer.commons.environments.Environments.my;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import snapps.blinkinglights.gui.BlinkingLightsGui;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import snapps.meter.bandwidth.gui.BandwidthMeterGui;
import snapps.meter.memory.gui.MemoryMeterGui;
import snapps.watchme.WatchMe;
import snapps.watchme.codec.ImageCodec;
import snapps.watchme.gui.WatchMeGui;
import snapps.watchme.gui.windows.RemoteWatchMeWindows;
import snapps.whisper.gui.WhisperGui;
import snapps.whisper.speex.Speex;
import snapps.whisper.speextuples.SpeexTuples;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.commons.environments.Environments;
import sneer.commons.io.StoragePath;
import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;
import sneer.container.NewContainers;
import sneer.kernel.container.SneerConfig;
import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clock.Clock;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.crypto.Crypto;
import sneer.pulp.datastore.DataStore;
import sneer.pulp.datastructures.cache.CacheFactory;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.logging.Logger;
import sneer.pulp.memory.MemoryMeter;
import sneer.pulp.network.Network;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.propertystore.PropertyStore;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.gates.logic.LogicGates;
import sneer.pulp.reactive.listsorter.ListSorter;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.serialization.Serializer;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.image.ImageFactory;
import sneer.skin.main_Menu.MainMenu;
import sneer.skin.menu.MenuFactory;
import sneer.skin.rooms.ActiveRoomKeeper;
import sneer.skin.screenshotter.Screenshotter;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.loopback.LoopbackTester;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.player.SoundPlayer;
import sneer.skin.sound.speaker.Speaker;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.Jars;


public class Sneer {

	private static final String USER_INFO_PROPERTIES_FILE = "user.info.properties";
	private static final String YOUR_OWN_NAME = "yourOwnName";
	private static final String DYN_DNS_USER = "dynDnsUser";
	private static final String DNY_DNS_PASSWORD = "dnyDnsPassword";
	private static NewContainer _container;

	public static void main(String[] args) throws Exception {
		tryRememberArguments();
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
		publishAllNecessaryBricks();

		Environments.runWith(container().environment(), new Runnable() { @Override public void run() {
//			BrickBundle bundle = my(Deployer.class).pack(new File("bricks/src"));
//			my(BrickManager.class).install(bundle);

			my(MainSneerBrick.class).start(ownName(args), dynDnsUser(args), dynDnsPassword(args));
		}});
	}

	private static void publishAllNecessaryBricks() throws BrickLoadingException, IOException {
		publishBricks(
				Logger.class,
				Serializer.class,
				ProbeManager.class,
				Speex.class,
				TupleFilterManager.class,
				SpeexTuples.class,
				ReachabilitySentinel.class,
				Updater.class,
				DynDnsAccountKeeper.class,
				PropertyStore.class,
				DataStore.class,
				HttpClient.class,
				CheckIp.class,
				OwnIpDiscoverer.class,
				DynDnsClient.class,
				Network.class,
				PortKeeper.class,
				SocketAccepter.class,
				SocketReceiver.class,
				InternetAddressKeeper.class,
				SocketOriginator.class,
				BandwidthCounter.class,
				Signals.class,
				MemoryMeter.class,
				RemoteWatchMeWindows.class,
				LogicGates.class,
				ActiveRoomKeeper.class,
				RetrierManager.class,
				Mic.class,
				Speaker.class,
				LoopbackTester.class,
				CacheFactory.class,
				ImageCodec.class,
				Screenshotter.class,
				WatchMe.class,
				Audio.class,
				SoundPlayer.class,
				SneerConfig.class,
				StoragePath.class,
				TupleSpace.class,
				Wind.class,
				SignalChooserManagerFactory.class,
				ListSorter.class,
				ContactComparator.class,
				ConnectionManager.class,
				ReactiveWidgetFactory.class,
				ContactActionManager.class,
				ContactManager.class,
				Crypto.class,
				KeyManager.class,
				BlinkingLights.class,
				InstrumentManager.class,
				MenuFactory.class,
				MainMenu.class,
				ImageFactory.class,
				Dashboard.class,
				EventNotifiers.class,
				ExceptionHandler.class,
				Clock.class,
				OwnNameKeeper.class,
				ThreadPool.class,
				ClockTicker.class,

				ContactsGui.class,
				WindGui.class,
				WatchMeGui.class,
				WhisperGui.class,
				MemoryMeterGui.class,
				BandwidthMeterGui.class,
				BlinkingLightsGui.class,
				
				MainSneerBrick.class
		);
	}

	private static void publishBricks(Class<?>... bricks) throws BrickLoadingException, IOException {
		for (Class<?> brick : bricks) {
//			System.err.println(brick);
			container().runBrick(Jars.directoryFor(brick));
		}
	}

	private static NewContainer container() {
		if (_container == null) _container = NewContainers.newContainer();
		return _container;
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