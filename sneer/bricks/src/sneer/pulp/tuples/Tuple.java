package sneer.pulp.tuples;

import sneer.pulp.keymanager.PublicKey;

public abstract class Tuple {

	public Tuple(PublicKey pPublisher, long pPublicationTime) {
		publisher = pPublisher;
		publicationTime = pPublicationTime;
	}
	
	public final PublicKey publisher;
	public final long publicationTime;
	
}
