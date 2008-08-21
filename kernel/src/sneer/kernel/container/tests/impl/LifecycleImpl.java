package sneer.kernel.container.tests.impl;

import sneer.kernel.container.Startable;
import sneer.kernel.container.tests.Lifecycle;

public class LifecycleImpl implements Lifecycle, Startable {
    
    private boolean _startCalled;

    @Override
    public void start() throws Exception {
        _startCalled = true;
    }

    @Override
    public boolean startCalled()
    {
        return _startCalled;
    }

}
