package sneer.bricks.mesh;

import java.util.Collection;

import sneer.lego.Brick;


public interface Me extends Party {

	<B extends Brick> Collection<B> allImmediateContactBrickCounterparts(Class<B> class1); //Refactor make SetSignal

}
