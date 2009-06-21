package sneer.tests.adapters;

import sneer.foundation.brickness.Brick;

/** This brick will have one instance loaded inside each Sneer party's container to control and observe the other bricks. This interface is separate from the SneerParty interface because it has to be loaded by the Sneer party's api ClassLoader, so that Brickness will load all bricks this one needs using that same api ClassLoader.*/
@Brick
public interface SneerPartyProbe {}
