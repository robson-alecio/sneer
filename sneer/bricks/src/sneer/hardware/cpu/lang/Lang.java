package sneer.hardware.cpu.lang;

import java.io.Serializable;
import java.util.Collection;

import sneer.brickness.Brick;

@Brick
public interface Lang {

	Arrays arrays(); 
	Serialization serialization(); 
	Strings strings();
	
	interface Arrays { 
		void reverse(Object[] array);
	}
	
	interface Serialization {
		byte[] serialize(Serializable obj) ;
		<T> T serialize(byte[] data);
	}
	
	interface Strings { 
		boolean isEmpty(String str);
		String join(Collection<?> collection, String separator);
		String trimToNull(String str);
		String chomp(String str, String separator);
		String deleteWhitespace(String str);
	}
}

