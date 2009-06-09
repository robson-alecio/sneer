package sneer.foundation.commons.environments;

public interface Environment {
	<T> T provide(Class<T> intrface);
}