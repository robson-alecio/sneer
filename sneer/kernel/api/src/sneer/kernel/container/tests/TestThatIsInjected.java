package sneer.kernel.container.tests;


import org.junit.Before;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import wheel.testutil.TestThatMightUseResources;

public class TestThatIsInjected extends TestThatMightUseResources {
	
    protected Object[] getBindings() {
    	return new Object[]{};
    }
    
	@Before
	final public void beforeTestThatIsInjected() throws Exception {
		injectDependencies();
	}

	private void injectDependencies() {
		Container container = ContainerUtils.newContainer(getBindings());
	    Injector injector = container.produce(Injector.class);
	    injector.inject(this);
	}
}
