package snapps.wind;

import sneer.pulp.keymanager.PublicKey;

public class Tuple {

	public Tuple(PublicKey pPublisher, long pPublicationTime) {
		publisher = pPublisher;
		publicationTime = pPublicationTime;
	}
	
	public final PublicKey publisher;
	public final long publicationTime;
	
}