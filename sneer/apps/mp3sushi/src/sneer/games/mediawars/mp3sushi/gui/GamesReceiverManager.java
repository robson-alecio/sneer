package sneer.games.mediawars.mp3sushi.gui;

import sneer.games.mediawars.mp3sushi.TheirsGame;
import wheel.lang.Casts;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class GamesReceiverManager implements ReceiverManager<TheirsGame> {

	public void addReceiverToSignals(Omnivore<?> receiver, TheirsGame game) {
		addReceiverToSignal(receiver, game.getStatus().output());
		addReceiverToSignal(receiver, game.getGameConfiguration().output());
		addReceiverToSignal(receiver, game.getPlayer().nick());
	}

	@Override
	public void removeReceiverFromSignals(Omnivore<?> receiver, TheirsGame game) {
		removeReceiverFromSignal(receiver, game.getStatus().output());
		removeReceiverFromSignal(receiver, game.getGameConfiguration().output());
		removeReceiverFromSignal(receiver, game.getPlayer().nick());
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
	public String getDisplay(TheirsGame object) {
		// Implement Auto-generated method stub
		return object.toString();
	}

}

