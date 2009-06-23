package sneer.bricks.software.code.metaclass.asm;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface ASM {

	ClassReader newClassReader(File classFile);
	Opcodes opcodes();
	
	interface Opcodes{
		int accInterface();
	}

}
