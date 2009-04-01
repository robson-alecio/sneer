package main.impl;

import static sneer.commons.environments.Environments.my;
import main.MainSneerBrick;
import snapps.blinkinglights.gui.BlinkingLightsGui;
import snapps.contacts.gui.ContactsGui;
import snapps.meter.bandwidth.gui.BandwidthMeterGui;
import snapps.meter.memory.gui.MemoryMeterGui;
import snapps.watchme.gui.WatchMeGui;
import snapps.watchme.gui.windows.RemoteWatchMeWindows;
import snapps.whisper.gui.WhisperGui;
import snapps.whisper.speextuples.SpeexTuples;
import snapps.wind.gui.WindGui;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.probe.ProbeManager;
import sneer.skin.dashboard.Dashboard;

class MainSneerBrickImpl implements MainSneerBrick {

	@SuppressWarnings("unused")
	private final ClockTicker _ticker = my(ClockTicker.class);

	@SuppressWarnings("unused")
	private final Dashboard _gui1 = my(Dashboard.class);
	
	@SuppressWarnings("unused")
	private final ContactsGui _gui2 = my(ContactsGui.class);
	
	@SuppressWarnings("unused")
	private final WindGui _gui3 = my(WindGui.class);
	
	@SuppressWarnings("unused")
	private final WatchMeGui _gui4 = my(WatchMeGui.class);

	@SuppressWarnings("unused")
	private final WhisperGui _gui5 = my(WhisperGui.class);

	@SuppressWarnings("unused")
	private final RemoteWatchMeWindows _gui6 = my(RemoteWatchMeWindows.class);

	@SuppressWarnings("unused")
	private final MemoryMeterGui _gui7 = my(MemoryMeterGui.class);

	@SuppressWarnings("unused")
	private final BandwidthMeterGui _gui8 = my(BandwidthMeterGui.class);

	@SuppressWarnings("unused")
	private final BlinkingLightsGui _lastGui = my(BlinkingLightsGui.class);
	
	@SuppressWarnings("unused")
	private final SocketOriginator _networkDaemon1 = my(SocketOriginator.class);
	
	@SuppressWarnings("unused")
	private final SocketReceiver _networkDaemon2 = my(SocketReceiver.class);

	@SuppressWarnings("unused")
	private final DynDnsClient _dynDns = my(DynDnsClient.class);
	
	@SuppressWarnings("unused")
	private final ReachabilitySentinel _reachabilitySentinel = my(ReachabilitySentinel.class);	
	
	@SuppressWarnings("unused")
	private final SpeexTuples _speexTuples = my(SpeexTuples.class);	

	@SuppressWarnings("unused")
	private final ProbeManager _probes = my(ProbeManager.class);


	
	{
		WelcomeWizard.showIfNecessary();
		
		my(Dashboard.class).show();
	}


}
