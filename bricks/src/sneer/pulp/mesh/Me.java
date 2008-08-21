package sneer.pulp.mesh;

import sneer.kernel.container.Brick;
import wheel.reactive.sets.SetSignal;


public interface Me extends Party {

	<B extends Brick> SetSignal<B> allImmediateContactBrickCounterparts(Class<B> brickType); //Refactor make SetSignal

}
