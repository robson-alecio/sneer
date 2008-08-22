package spikes.kernel.container.impl.classloader.enhancer;

import java.io.Serializable;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import sneer.kernel.container.impl.classloader.enhancer.Enhancer;

public class MakeSerializable implements Enhancer {
	
	static class MakeSerializableVisitor extends ClassAdapter {

		private boolean _containsSerialVersionUID;
		
		private boolean _isInterface;
		
		private String _className;

		public MakeSerializableVisitor(ClassVisitor classVisitor) {
			super(classVisitor);
		}
		
		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

			_isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
			_className = name.replaceAll("/", ".");
			_className.toString();
			
			String typeName = Type.getType(Serializable.class).getInternalName();
			if(!isSerializable(interfaces, typeName))
				interfaces = addToArray(interfaces, typeName);
			
			super.visit(version, access, name, signature, superName, interfaces);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			if (name.equals("serialVersionUID")) {
				_containsSerialVersionUID = true;
			}
			return super.visitField(access, name, desc, signature, value);
		}
		
		@Override
		public void visitEnd() {
			if (!_isInterface && !_containsSerialVersionUID) {
				//System.out.println(_className + " enhanced");
				super.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "serialVersionUID", Type.LONG_TYPE.getDescriptor(), null, new Long(1L));
			}
			super.visitEnd();
		}

		private String[] addToArray(String[] array, String value) {
			int length = array.length;
			String[] result = new String[length + 1];
			System.arraycopy(array, 0, result, 0, length);
			result[length] = value;
			return result;
		}

		private boolean isSerializable(String[] interfaces, String serializableType) {
			for(String intrface : interfaces) {
				if(intrface.equals(serializableType)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public ClassVisitor enhance(ClassVisitor visitor) {
		return new MakeSerializableVisitor(visitor);
	}

}
