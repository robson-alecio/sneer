package main;

import java.io.IOException;

import snapps.blinkinglights.gui.BlinkingLightsGui;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.comparator.ContactComparator;
import snapps.contacts.hardcoded.HardcodedContacts;
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
import sneer.commons.io.StoragePath;
import sneer.container.BrickLoadingException;
import sneer.container.Container;
import sneer.container.Containers;
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
import sneer.pulp.logging.robust.RobustLogger;
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


	public static void main(String[] ignored) throws Exception {
		publishBricks(
				SneerConfig.class,
				StoragePath.class,
				ExceptionHandler.class,
				OwnNameKeeper.class,
				ThreadPool.class,
				Clock.class,
				Crypto.class,
				EventNotifiers.class,
				KeyManager.class,
				BlinkingLights.class,
				ContactManager.class,
				TupleFilterManager.class,
				TupleSpace.class,
				ConnectionManager.class,
				Logger.class,
				RobustLogger.class,
				Serializer.class,
				ProbeManager.class,
				Speex.class,
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
				Wind.class,
				SignalChooserManagerFactory.class,
				ListSorter.class,
				ContactComparator.class,
				ReactiveWidgetFactory.class,
				ContactActionManager.class,
				InstrumentManager.class,
				MenuFactory.class,
				MainMenu.class,
				ImageFactory.class,
				ClockTicker.class,

				ContactsGui.class,
				WindGui.class,
				WatchMeGui.class,
				WhisperGui.class,
				MemoryMeterGui.class,
				BandwidthMeterGui.class,
				BlinkingLightsGui.class,

				Dashboard.class,
				
				HardcodedContacts.class
		);
	}

	static void publishBricks(Class<?>... bricks) throws BrickLoadingException, IOException {
		Container container = Containers.newContainer();

		for (Class<?> brick : bricks)
			container.runBrick(Jars.directoryFor(brick));
	}

}