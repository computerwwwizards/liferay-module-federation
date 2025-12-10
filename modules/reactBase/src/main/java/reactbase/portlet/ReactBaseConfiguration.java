package reactbase.portlet;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
	category = "sample",
	scope = ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE
)
@Meta.OCD(
	id = "reactbase.portlet.ReactBaseConfiguration",
	localization = "content/Language",
	name = "react-base-configuration-name",
	description = "react-base-configuration-description"
)
public interface ReactBaseConfiguration {

	@Meta.AD(
		deflt = "",
		description = "module-federation-remote-url-description",
		name = "module-federation-remote-url",
		required = false
	)
	public String moduleFederationRemoteURL();

}
