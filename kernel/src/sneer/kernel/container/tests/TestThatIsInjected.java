package sneer.kernel.container.tests;


import org.junit.Before;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
import wheel.testutil.TestThatMightUseFiles;

public class TestThatIsInjected extends TestThatMightUseFiles {
	
    protected Object[] getBindings() {
    	return new Object[]{};
    }
    
	@Before
	final public void beforeTestThatIsInjected() throws Exception {
		injectDependencies();
	}

	private void injectDependencies() {
		Container container = ContainerUtils.newContainer(getBindings());
	    Injector injector = new AnnotatedFieldInjector(container);

	    injector.inject(this);
	}
}
