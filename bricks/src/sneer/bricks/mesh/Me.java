package sneer.bricks.mesh;

import sneer.lego.Brick;
import wheel.reactive.sets.SetSignal;


public interface Me extends Party {

	<B extends Brick> SetSignal<B> allImmediateContactBrickCounterparts(Class<B> brickType); //Refactor make SetSignal

}
