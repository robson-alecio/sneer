package sneer.lego.impl;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import sneer.lego.ConfigurationFactory;

public class NullConfigurationFactory
    implements ConfigurationFactory
{
    private static final ConfigurationFactory INSTANCE = new NullConfigurationFactory();
    
    private NullConfigurationFactory()
    {}
    
    public static ConfigurationFactory instance() {
        return INSTANCE;
    }
    
    @Override
    public Configuration getConfiguration(Class<?> clazz) {
        return new BaseConfiguration();
    }
}
