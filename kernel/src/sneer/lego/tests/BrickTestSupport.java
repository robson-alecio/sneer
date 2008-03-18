package sneer.lego.tests;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;

import sneer.lego.Binder;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.FieldInjector;
import sneer.lego.impl.Injector;
import wheel.lang.Threads;

public class BrickTestSupport {
	
	private static Container container;

    protected Container getContainer()
    	throws Exception
    {
    	if(container == null)
    	{
    		Binder binder = getBinder();
    		container = ContainerUtils.getContainer(binder, null);
    	}
    	return container;
    }

    protected Binder getBinder() {
    	return null;
    }
    
	@Before
	public void injectDependencies()
	    throws Exception
	{
	    Injector injector = new FieldInjector(getContainer());
	    injector.inject(this);
	}

	public static File getWorkDirectory() {
		URL url = Threads.contextClassLoader().getResource(".");
		String dir = FilenameUtils.concat(url.getFile(), ".work"); 
		File file = new File(dir);
		if(!file.exists()) 
			file.mkdirs();
		return file;
	}

	protected void stopContainer() {
		container = null;
	}
}
