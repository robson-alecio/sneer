package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public class Shout {

	public final String _phrase;
	public final PublicKey _publisher;

	public Shout(String phrase, PublicKey publisher) {
		_phrase = phrase;
		_publisher = publisher;
		System.out.println("Shouting " + publisher.toString());
	}


}
