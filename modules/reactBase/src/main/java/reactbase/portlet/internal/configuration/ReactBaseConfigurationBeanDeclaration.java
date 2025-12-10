package reactbase.portlet.internal.configuration;

import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;

import org.osgi.service.component.annotations.Component;

import reactbase.portlet.ReactBaseConfiguration;

@Component
public class ReactBaseConfigurationBeanDeclaration
	implements ConfigurationBeanDeclaration {

	@Override
	public Class<?> getConfigurationBeanClass() {
		return ReactBaseConfiguration.class;
	}

}
