package sneer.foundation.environments;

/** Interfaces that extend this one are a type-safe way of configuring properties for execution in environments.*/
public interface EnvironmentProperty<T> {
	
	T get();
	
}
