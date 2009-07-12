package sneer.bricks.software.timing;

public interface PropertySetter {

	<T> Animator animator(int duration,  Object object, String propertyName, T... params);

}