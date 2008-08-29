package sneer.kernel.container.tests;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
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
