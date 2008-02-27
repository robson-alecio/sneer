package sneer.lego.tests;

import org.junit.Before;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.FieldInjector;
import sneer.lego.impl.Injector;

public class BrickTestSupport {
	
	private static Container container;

    protected Container getContainer()
    	throws Exception
    {
    	if(container == null)
    	{
    		container = ContainerUtils.getContainer();
    	}
    	return container;
    }

	@Before
	public void injectDependencies()
	    throws Exception
	{
	    Injector injector = new FieldInjector(getContainer());
	    injector.inject(this);
	}

}
