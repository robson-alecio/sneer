package sneer.bricks.software.code.metaclass.asm.impl;

import java.io.File;

import sneer.bricks.software.code.metaclass.asm.ASM;
import sneer.bricks.software.code.metaclass.asm.ClassReader;

class ASMImpl implements ASM{

	Opcodes _opcodes = new OpcodesImpl();
	
	@Override
	public ClassReader newClassReader(File classFile) {
		return new ClassReaderImpl(classFile);
	}

	@Override
	public Opcodes opcodes() {
		return _opcodes;
	}
	
	class OpcodesImpl implements Opcodes{
		
		@Override public int accInterface() { return org.objectweb.asm.Opcodes.ACC_INTERFACE; }
		
	}
}

