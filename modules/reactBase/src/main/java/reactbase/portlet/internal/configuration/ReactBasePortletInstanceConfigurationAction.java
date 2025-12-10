package reactbase.portlet.internal.configuration;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import reactbase.constants.ReactBasePortletKeys;

@Component(
	property = "javax.portlet.name=" + ReactBasePortletKeys.ReactBase,
	service = ConfigurationAction.class
)
public class ReactBasePortletInstanceConfigurationAction
	extends DefaultConfigurationAction {

	@Override
	public String getJspPath(HttpServletRequest httpServletRequest) {
		return "/configuration.jsp";
	}

	@Override
	public void processAction(
		PortletConfig portletConfig, ActionRequest actionRequest,
		ActionResponse actionResponse)
	throws Exception {

		String moduleFederationRemoteURL = ParamUtil.getString(
			actionRequest, "moduleFederationRemoteURL");

		setPreference(
			actionRequest, "moduleFederationRemoteURL",
			moduleFederationRemoteURL);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=reactbase)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

}
