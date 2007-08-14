package sneer.games.mediawars.mp3sushi.gui;

import sneer.games.mediawars.mp3sushi.PlayerIdentification;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class PlayersReceiverManager implements ReceiverManager<PlayerIdentification> {

	public void addReceiverToSignals(Omnivore<?> receiver, PlayerIdentification player) {
		addReceiverToSignal(receiver, player.getNick());
	}

	@Override
	public void removeReceiverFromSignals(Omnivore<?> receiver, PlayerIdentification player) {
		removeReceiverFromSignal(receiver, player.getNick());
	}

	private <T> void addReceiverToSignal(Omnivore<?> receiver, Signal<T> signal) {
		Omnivore<T> castedReceiver = Casts.uncheckedGenericCast(receiver);
		signal.addReceiver(castedReceiver);
	}
	
	private <T> void removeReceiverFromSignal(Omnivore<?> receiver, Signal<T> signal) {
		Omnivore<T> casted = Casts.uncheckedGenericCast(receiver);
		signal.removeReceiver(casted);
	}

	@Override
	public String getDisplay(PlayerIdentification object) {
		// Implement Auto-generated method stub
		return object.toString();
	}

}

