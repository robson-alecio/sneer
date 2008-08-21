package sneer.kernel.container.impl;

import java.lang.reflect.Field;

import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;

public class AnnotatedFieldInjector extends FieldInjector	 {

	public AnnotatedFieldInjector(Container container) {
		super(container);
	}

	@Override
	protected void injectOnField(Object obj, Field field) {
		Inject inject = field.getAnnotation(Inject.class);
		if(inject != null) {
		    Object component = _container.produce(field.getType());
		    setValueOnField(obj, field, component);
		}
	}
}
