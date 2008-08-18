package sneerapps.wind;

import sneer.pulp.keymanager.PublicKey;

public class Affinity extends Tuple {

	@Override
	public String toString() {
		return publisher.toString() + " " + percentage + " " + peer;
	}

	public final float percentage;
	public final PublicKey peer;

	public Affinity(PublicKey pPublisher, long pPublicationTime, PublicKey pPeer, float pPercentage) {
		super(pPublisher, pPublicationTime);
		percentage = pPercentage;
		peer = pPeer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((peer == null) ? 0 : peer.hashCode());
		result = prime * result + Float.floatToIntBits(percentage);
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
		if (getClass() != obj.getClass()) {
			System.out.println(getClass() + " " + obj.getClass());
			return false;
		}
		Affinity other = (Affinity) obj;
		if (peer == null) {
			if (other.peer != null)
				return false;
		} else if (!peer.equals(other.peer))
			return false;
		if (Float.floatToIntBits(percentage) != Float
				.floatToIntBits(other.percentage))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		return true;
	}

	

}
