package spikes.igor;

import java.util.ArrayList;
import java.util.List;

public class ClassBlueprint {

	private String name;

	private String superclass;

	private List<String> interfaces;

	private List<String> attributes;

	private List<String> methods;

	public ClassBlueprint() {
		super();
		this.interfaces = new ArrayList<String>(0);
		this.attributes = new ArrayList<String>(0);
		this.methods = new ArrayList<String>(0);
	}

	public ClassBlueprint(String className) {
		this();
		this.setName(className);
	}

	public ClassBlueprint(String className, String superclassName) {
		this(className);
		this.setSuperclass(superclassName);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public String getSuperclass() {
		return this.superclass;
	}

	public void setSuperclass(String superclassName) {
		this.superclass = superclassName;
	}

	public String[] getInterfaces() {
		return this.interfaces.toArray(new String[0]);
	}

	public void addInterface(String interfaceName) {
		this.interfaces.add(interfaceName);
	}

	public String[] getMethods() {
		return this.methods.toArray(new String[0]);
	}

	public void addMethod(String methodDefinition) {
		this.methods.add(methodDefinition);
	}

	public String[] getAttributes() {
		return this.attributes.toArray(new String[0]);
	}

	public void addAttribute(String variableName) {
		this.attributes.add(variableName);
	}

	@Override
	public String toString() {
		return this.getName() + "'s blueprint";
	}
}
