package sneer.kernel.container.tests;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
import sneer.kernel.container.impl.classloader.ApiClassLoader;
import wheel.lang.Threads;

public class BrickTestSupport {
	
    protected Object[] getBindings() {
    	return new Object[]{};
    }
    
	@Before
	public void injectDependencies()throws Exception {
		System.out.println("Before pai");

		Container container = ContainerUtils.newContainer(getBindings());
	    Injector injector = new AnnotatedFieldInjector(container);

	    injector.inject(this);
	    
		ApiClassLoader.checkAllInstancesAreFreed();

		new ApiClassLoader(null, getClass().getClassLoader());
		ApiClassLoader.checkAllInstancesAreFreed();

	}

	@After
	public void after() {
		System.out.println("After pai");
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
