<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />

<%
String moduleFederationRemoteURL = portletPreferences.getValue(
	"moduleFederationRemoteURL", "");
%>

<div class="portlet-configuration-body-content p-3">
	<aui:form
		action="<%= configurationActionURL %>"
		cssClass="configuration-form"
		method="post"
		name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

		<aui:fieldset cssClass="mb-4">
			<aui:input
				cssClass="w-100"
				label="module-federation-remote-url"
				name="moduleFederationRemoteURL"
				type="text"
				value="<%= moduleFederationRemoteURL %>" />
		</aui:fieldset>

		<aui:button-row>
			<aui:button cssClass="btn btn-primary" type="submit" />
			<aui:button onClick="<%= portletDisplay.getURLConfigurationJS() %>" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>
