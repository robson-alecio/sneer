package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public class Shout extends Tuple {

	public final String phrase;

	public Shout(String pPhrase, PublicKey pPublisher) {
		super(pPublisher);
		phrase = pPhrase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phrase == null) ? 0 : phrase.hashCode());
		result = prime * result
				+ ((publisher == null) ? 0 : publisher.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shout other = (Shout) obj;
		if (phrase == null) {
			if (other.phrase != null)
				return false;
		} else if (!phrase.equals(other.phrase))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return phrase;
	}

}
