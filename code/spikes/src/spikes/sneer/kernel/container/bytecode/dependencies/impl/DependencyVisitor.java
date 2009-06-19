package spikes.sneer.kernel.container.bytecode.dependencies.impl;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;


class DependencyVisitor extends EmptyVisitor {

	private final List<String> _dependencies = new ArrayList<String>();
	private String _type;
	
	@Override
	public void visitLdcInsn(Object constValue) {
		if (constValue instanceof Type)
			_type = ((Type)constValue).getClassName();
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
		if (!"my".equals(name))
			return;
		
		_dependencies.add(_type);
	}
	
	public List<String> dependencies() {
		return _dependencies;
	}
}