package sneer.kernel.container.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import sneer.kernel.container.Container;
import sneer.kernel.container.Injector;
import sneer.kernel.container.LegoException;
import sneer.kernel.container.utils.FieldUtils;

public abstract class FieldInjector implements Injector {
    
	protected Container _container;
    
    public FieldInjector(Container container) {
    	_container = container;
    }

	@Override
	public void inject(Object obj) throws LegoException {
        List<Field> fields = FieldUtils.getAllFields(obj.getClass());
        for (Field field : fields) {
            injectOnField(obj, field);
        }
	}

	@Override
	public void inject(Class<?> clazz) throws LegoException {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if(Modifier.isStatic(field.getModifiers()))
				injectOnField(null, field);
		}
	}

	protected void setValueOnField(Object obj, Field field, Object component) {
		boolean before = field.isAccessible();
		field.setAccessible(true);
		try {
			Object value = field.get(obj);
			if(value == null) field.set(obj, component);
		} catch (Exception e) {
			throw new LegoException("error injecting component into field: "+field.getName(),e);
		} finally {
			field.setAccessible(before);
		}
	}

	protected abstract void injectOnField(Object obj, Field field);

}
