package sneer.skin.viewmanager;

public interface ViewManager {

	void register(PartyView app);

	PartyView getOnlyOnePartyViewForNow();
	
}
