package sneer.bricks.software.code.metaclass.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import sneer.bricks.software.code.metaclass.asm.ASM;
import sneer.bricks.software.code.metaclass.asm.ClassReader;
import sneer.bricks.software.code.metaclass.asm.ClassVisitor;

class ASMMetaClass extends MetaClassSupport {

	private boolean _loaded = false;

	public ASMMetaClass(File root, File classFile) {
		super(root, classFile);
	}

	@Override
	public String getName() {
		lazyLoad();
		return _className;
	}

	@Override
	public String getPackageName() {
		lazyLoad();
		return _packageName;
	}

	@Override
	public boolean isInterface() {
		lazyLoad();
		return _isInterface;
	}


	@Override
	protected void parse() {
		//do nothing
	}

	private void lazyLoad() {
		if (_loaded) 
			return;

		ClassReader _reader = my(ASM.class).newClassReader(_classFile);
		_reader.accept(new ClassVisitor(){  @Override  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			_isInterface = (access & my(ASM.class).opcodes().accInterface()) != 0;
			_className = name.replaceAll("/", ".");
			_packageName = _className.substring(0, _className.lastIndexOf("."));		
			_loaded=true;
		}});
	}
}
