package sneer.kernel.container.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Container;

public class StaticFieldInjector extends FieldInjector	 {

	public StaticFieldInjector(Container container) {
		super(container);
	}

	@Override
	protected void injectOnField(Object obj, Field field) {
		Class<?> type = field.getType();
		
		if (!Modifier.isStatic(field.getModifiers())) return;
		if (!type.isInterface()) return;
		if (!Brick.class.isAssignableFrom(type)) return;
		
		Object component = _container.produce(type);
		setValueOnField(obj, field, component);
	}
}
