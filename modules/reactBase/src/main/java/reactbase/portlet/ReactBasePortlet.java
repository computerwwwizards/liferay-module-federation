package reactbase.portlet;

import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import reactbase.constants.ReactBasePortletKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author ubuntu
 */
@Component(
	configurationPid = "reactbase.portlet.ReactBaseConfiguration",
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"javax.portlet.display-name=X7Y2 Portlet",
		"com.liferay.portlet.header-portlet-css=/css/index.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + ReactBasePortletKeys.ReactBase,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class ReactBasePortlet extends MVCPortlet {
	@Reference
	private ConfigurationProvider _configurationProvider;

	@Override
	public void render(
		RenderRequest renderRequest, RenderResponse renderResponse)
	throws IOException, PortletException {

		try {
			ReactBaseConfiguration configuration =
				_getReactBasePortletInstanceConfiguration(renderRequest);

			renderRequest.setAttribute(
				ReactBaseConfiguration.class.getName(), configuration);

			renderRequest.setAttribute(
				"moduleFederationRemoteURL",
				configuration.moduleFederationRemoteURL());
		}
		catch (ConfigurationException configurationException) {
			throw new PortletException(configurationException);
		}

		super.render(renderRequest, renderResponse);
	}
	

	private ReactBaseConfiguration _getReactBasePortletInstanceConfiguration(
		RenderRequest renderRequest)
	throws ConfigurationException {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return _configurationProvider.getPortletInstanceConfiguration(
			ReactBaseConfiguration.class, themeDisplay.getLayout(),
			themeDisplay.getPortletDisplay().getId());
	}

}