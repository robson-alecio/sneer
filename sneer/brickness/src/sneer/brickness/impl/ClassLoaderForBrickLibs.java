package sneer.brickness.impl;

import java.net.URL;
import java.util.List;

import sneer.brickness.Nature;

/** To be implemented.*/
class ClassLoaderForBrickLibs extends ClassLoaderWithNatures {

	ClassLoaderForBrickLibs(URL[] urls, ClassLoader next, List<Nature> natures) {
		super(urls, next, natures);
		// Implement Auto-generated constructor stub
	}

	@Override
	protected boolean isEagerToLoad(String className) {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}	


}
