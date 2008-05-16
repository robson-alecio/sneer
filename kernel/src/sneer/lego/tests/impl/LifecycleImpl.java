package sneer.lego.tests.impl;

import sneer.lego.Startable;
import sneer.lego.tests.Lifecycle;

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
