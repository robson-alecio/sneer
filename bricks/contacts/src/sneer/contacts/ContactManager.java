package sneer.contacts;

import functionaltests.SovereignParty;

public interface ContactManager {
    
    boolean add(SovereignParty party); //FixUrgent: move SovereignParty to this package

    void alias(SovereignParty party, String nickname);
}