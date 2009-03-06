package sneer.commons.environments;



public class ClosedEnvironment implements Environment {

	private Object[] _bindings;

	public ClosedEnvironment(Object[] bindings) {
		_bindings = bindings;
	}

	@Override
	public <T> T provide(Class<T> intrface) {
		for (Object binding : _bindings)
			if (intrface.isInstance(binding))
				return (T) binding;
		return null;
	}

}
