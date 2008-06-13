package spikes.danielsantos.things;

import java.util.ArrayList;
import java.util.Collection;

public class Finder {

	public Collection<Thing> find(Collection<Thing> things, String tags) {
		ArrayList<Thing> result = new ArrayList<Thing>();
		for (Thing candidate : things) {
			if (candidate.name().toUpperCase().indexOf(tags.toUpperCase()) != -1)
				result.add(candidate);
			if (candidate.description().toUpperCase().indexOf(tags.toUpperCase()) != -1)
				result.add(candidate);
		}
		return result;
	}

}
