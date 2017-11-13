package me.spring.act.reactor.configuration.multiDatasource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;

public class RelaxedPropertyResolver implements PropertyResolver {

	private final PropertyResolver resolver;

	private final String prefix;

	public RelaxedPropertyResolver(PropertyResolver resolver) {
		this(resolver, null);
	}

	public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
		Assert.notNull(resolver, "PropertyResolver must not be null");
		this.resolver = resolver;
		this.prefix = (prefix == null ? "" : prefix);
	}

	@Override
	public String getRequiredProperty(String key) throws IllegalStateException {
		return getRequiredProperty(key, String.class);
	}

	@Override
	public <T> T getRequiredProperty(String key, Class<T> targetType)
			throws IllegalStateException {
		T value = getProperty(key, targetType);
		Assert.state(value != null, String.format("required key [%s] not found", key));
		return value;
	}

	@Override
	public String getProperty(String key) {
		return getProperty(key, String.class, null);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return getProperty(key, String.class, defaultValue);
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType) {
		return getProperty(key, targetType, null);
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		RelaxedNames prefixes = new RelaxedNames(this.prefix);
		RelaxedNames keys = new RelaxedNames(key);
		for (String prefix : prefixes) {
			for (String relaxedKey : keys) {
				if (this.resolver.containsProperty(prefix + relaxedKey)) {
					return this.resolver.getProperty(prefix + relaxedKey, targetType);
				}
			}
		}
		return defaultValue;
	}

	public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
		RelaxedNames prefixes = new RelaxedNames(this.prefix);
		RelaxedNames keys = new RelaxedNames(key);
		for (String prefix : prefixes) {
			for (String relaxedKey : keys) {
				if (this.resolver.containsProperty(prefix + relaxedKey)) {
					return ((RelaxedPropertyResolver) this.resolver).getPropertyAsClass(prefix + relaxedKey,
							targetType);
				}
			}
		}
		return null;
	}

	@Override
	public boolean containsProperty(String key) {
		RelaxedNames prefixes = new RelaxedNames(this.prefix);
		RelaxedNames keys = new RelaxedNames(key);
		for (String prefix : prefixes) {
			for (String relaxedKey : keys) {
				if (this.resolver.containsProperty(prefix + relaxedKey)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String resolvePlaceholders(String text) {
		throw new UnsupportedOperationException(
				"Unable to resolve placeholders with relaxed properties");
	}

	@Override
	public String resolveRequiredPlaceholders(String text)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException(
				"Unable to resolve placeholders with relaxed properties");
	}

	/**
	 * Return a Map of all values from all underlying properties that start with the
	 * specified key. NOTE: this method can only be used if the underlying resolver is a
	 * {@link ConfigurableEnvironment}.
	 * @param keyPrefix the key prefix used to filter results
	 * @return a map of all sub properties starting with the specified key prefix.
	 * @see PropertySourceUtils#getSubProperties
	 */
	public Map<String, Object> getSubProperties(String keyPrefix) {
		Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver,
				"SubProperties not available.");
		ConfigurableEnvironment env = (ConfigurableEnvironment) this.resolver;
		return getSubProperties(env.getPropertySources(), this.prefix,
				keyPrefix);
	}

	/**
	 * Return a Map of all values from the specified {@link PropertySources} that start
	 * with a particular key.
	 * @param propertySources the property sources to scan
	 * @param rootPrefix a root prefix to be prepended to the keyPrefix (can be
	 * {@code null})
	 * @param keyPrefix the key prefixes to test
	 * @return a map of all sub properties starting with the specified key prefixes.
	 * @see #getSubProperties(PropertySources, String, String)
	 */
	public Map<String, Object> getSubProperties(PropertySources propertySources,
			String rootPrefix, String keyPrefix) {
		RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
		Map<String, Object> subProperties = new LinkedHashMap<String, Object>();
		for (PropertySource<?> source : propertySources) {
			if (source instanceof EnumerablePropertySource) {
				for (String name : ((EnumerablePropertySource<?>) source)
						.getPropertyNames()) {
					String key = getSubKey(name, rootPrefix,
							keyPrefixes);
					if (key != null && !subProperties.containsKey(key)) {
						subProperties.put(key, source.getProperty(name));
					}
				}
			}
		}
		return Collections.unmodifiableMap(subProperties);
	}
	
	private String getSubKey(String name, String rootPrefixes,
			RelaxedNames keyPrefix) {
		rootPrefixes = (rootPrefixes == null ? "" : rootPrefixes);
		for (String rootPrefix : new RelaxedNames(rootPrefixes)) {
			for (String candidateKeyPrefix : keyPrefix) {
				if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
					return name.substring((rootPrefix + candidateKeyPrefix).length());
				}
			}
		}
		return null;
	}
}
