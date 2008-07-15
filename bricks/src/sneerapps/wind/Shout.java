package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public class Shout {

	public final String _phrase;
	public final PublicKey _publisher;

	public Shout(String phrase, PublicKey publisher) {
		_phrase = phrase;
		_publisher = publisher;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_phrase == null) ? 0 : _phrase.hashCode());
		result = prime * result
				+ ((_publisher == null) ? 0 : _publisher.hashCode());
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
		if (_phrase == null) {
			if (other._phrase != null)
				return false;
		} else if (!_phrase.equals(other._phrase))
			return false;
		if (_publisher == null) {
			if (other._publisher != null)
				return false;
		} else if (!_publisher.equals(other._publisher))
			return false;
		return true;
	}


}
