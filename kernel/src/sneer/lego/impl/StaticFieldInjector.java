package sneer.lego.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sneer.lego.Brick;
import sneer.lego.Container;

public class StaticFieldInjector extends FieldInjector	 {

	public StaticFieldInjector(Container container) {
		super(container);
	}

	@Override
	protected void injectOnField(Object obj, Field field) {
		Class<?> type = field.getType();
		
		if(!Modifier.isStatic(field.getModifiers())
				|| !type.isInterface()
				|| !Brick.class.isAssignableFrom(type)) {
			System.out.println("Field "+field.getName()+" not injected." 
					+ " static? " + Modifier.isStatic(field.getModifiers()) 
					+ ", interface? " + type.isInterface()
					+ ", brick? " + Brick.class.isAssignableFrom(type));
			return;
		}
		
		Object component = _container.produce(type);
		setValueOnField(obj, field, component);
	}
}
