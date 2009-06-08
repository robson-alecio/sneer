package spikes.sneer.pulp.deployer.impl.parser;

import java.io.File;


public class JavaSource {
	
	private String _unitType;

	private String _accessType;
	
	private String _typeName;
	
	public JavaSource(File sourceFile) {
		_typeName = sourceFile.getName();
	}

	public boolean isInterface() {
		return "interface".equals(_unitType);
	}

	public boolean isAccessPublic() {
		return "public".equals(_accessType);
	}

	public void setType(String type) {
		_unitType = type;
	}

	public void setAccessType(String accessType) {
		_accessType = accessType;
	}
	
	@Override
	public String toString() {
		return (isInterface() ? "[I]" : "[C]") + " " + _accessType + " " + _typeName ;
	}
}
