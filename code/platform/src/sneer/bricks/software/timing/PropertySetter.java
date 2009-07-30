package sneer.bricks.software.timing;

public interface PropertySetter {

	<T> Animator animator(int duration, Object object, String propertyName, T ... params);
	<T> Animator animator(int forwardDuration, int backwardDuration,  Object object, String propertyName, T... params);

}