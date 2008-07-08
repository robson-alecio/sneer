package sneer.lego.tests;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Injector;
import sneer.lego.impl.AnnotatedFieldInjector;
import wheel.lang.Threads;

public class BrickTestSupport {
	
    protected Object[] getBindings() {
    	return new Object[]{};
    }
    
	@Before
	public void injectDependencies()throws Exception {
		Container container = ContainerUtils.newContainer(getBindings());
	    Injector injector = new AnnotatedFieldInjector(container);
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
}
