package sneer.lego.tests.impl;

import org.apache.commons.configuration.Configuration;

import sneer.lego.Configurable;
import sneer.lego.Startable;
import sneer.lego.tests.Lifecycle;

public class LifecycleImpl implements Lifecycle, Configurable, Startable {
    
    private Configuration _configuration;
    
    private boolean _configureCalled;
    
    private boolean _startCalled;
    
    @Override
    public void configure(Configuration config) {
        _configureCalled = true;
        _configuration = config;
    }

    @Override
    public void start() throws Exception {
        _startCalled = true;
    }

    @Override
    public boolean configureCalled()
    {
        return _configureCalled;
    }

    @Override
    public boolean startCalled()
    {
        return _startCalled;
    }

}
