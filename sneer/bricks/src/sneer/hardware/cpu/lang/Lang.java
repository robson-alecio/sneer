package sneer.hardware.cpu.lang;

import sneer.brickness.Brick;

@Brick
public interface Lang {

	Arrays arrays();
	
	interface Arrays {
		void reverse(Object[] array);
	}
}
