package sneer.bricks.software.code.metaclass.asm.impl;

import java.io.File;

import sneer.bricks.software.code.metaclass.asm.ASM;
import sneer.bricks.software.code.metaclass.asm.ClassReader;

class ASMImpl implements ASM{

	@Override
	public ClassReader newClassReader(File classFile) {
		return null;
	}

	@Override
	public Opcodes opcodes() {
		return null;
	}

}
