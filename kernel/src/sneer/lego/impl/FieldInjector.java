package sneer.lego.impl;

import java.lang.reflect.Field;
import java.util.List;

import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.Injector;
import sneer.lego.LegoException;
import sneer.lego.utils.FieldUtils;

public class FieldInjector 
    implements Injector
{
    private Container _container;
    
    public FieldInjector(Container container) {
    	_container = container;
    }

    public void inject(Object obj) throws LegoException {
        List<Field> fields = FieldUtils.getAllFields(obj.getClass());
        for (Field field : fields) {
            Brick inject = field.getAnnotation(Brick.class);
            if(inject != null) {
                Object component = _container.produce(field.getType());
                boolean before = field.isAccessible();
                field.setAccessible(true);
                try {
					field.set(obj, component);
				} catch (Exception e) {
					throw new LegoException("error injection component into field: "+field.getName(),e);
				} finally {
					field.setAccessible(before);
				}
            }
        }
    }
}
