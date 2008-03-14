package sneer.lego;

import org.apache.commons.configuration.Configuration;

public interface ConfigurationFactory
{
    Configuration getConfiguration(Class<?> clazz);
}
