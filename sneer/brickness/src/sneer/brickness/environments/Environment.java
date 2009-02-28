package sneer.brickness.environments;

public interface Environment {
	<T> T provide(Class<T> intrface);
}