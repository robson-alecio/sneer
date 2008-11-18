package tests;


import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import wheel.testutil.TestThatMightUseResources;

public class TestThatIsInjected extends TestThatMightUseResources {
	
	private Object[] getMyBindings() {
		return ArrayUtils.add(getBindings(), new PersistenceConfigMock(tmpDirectory()));
	}

	protected Object[] getBindings() {
    	return new Object[]{};
    }
    
	@Before
	final public void beforeTestThatIsInjected() throws Exception {
		injectDependencies();
	}

	private void injectDependencies() {
		Container container = ContainerUtils.newContainer(getMyBindings());
	    Injector injector = container.produce(Injector.class);
	    injector.inject(this);
	}

}
