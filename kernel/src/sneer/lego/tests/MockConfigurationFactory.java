package sneer.lego.tests;

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;

import sneer.lego.ConfigurationFactory;

public class MockConfigurationFactory
    implements ConfigurationFactory
{
    private Map<String, Object> _map;

    public MockConfigurationFactory(Map<String, Object> values)
    {
        _map = values;
    }

    @Override
    public Configuration getConfiguration(Class<?> clazz)
    {
        return new MapConfiguration(_map);
    }

}
