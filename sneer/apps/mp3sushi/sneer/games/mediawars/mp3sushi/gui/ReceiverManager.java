package sneer.games.mediawars.mp3sushi.gui;

import wheel.lang.Omnivore;

public interface ReceiverManager<T> {


	public void removeReceiverFromSignals(Omnivore<?> receiver, T object );
	public void addReceiverToSignals(Omnivore<?> receiver, T object );
	public String getDisplay(T object);
}
