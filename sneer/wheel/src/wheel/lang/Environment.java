package wheel.lang;

public interface Environment {
	<T> T provide(Class<T> intrface);
}