package sneer.foundation.brickness;

import java.util.Arrays;

public class ClassDefinition {

	public final String name;
	public final byte[] bytes;

	public ClassDefinition(String name_, byte[] bytes_) {
		name = name_;
		bytes = bytes_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassDefinition other = (ClassDefinition) obj;
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
