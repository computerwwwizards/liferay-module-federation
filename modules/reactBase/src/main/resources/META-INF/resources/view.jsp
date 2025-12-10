<%@ include file="/init.jsp" %>

<%
	String moduleFederationRemoteURL =
		(String)request.getAttribute("moduleFederationRemoteURL");
%>

<div id="<portlet:namespace />-root"></div>

<aui:script>
	import(
		Liferay.ThemeDisplay.getPathContext() + '/o/reactBase/js/index.js'
	).then(
		(module) =>
			module.default(
				'<portlet:namespace />-root',
				'<%= (moduleFederationRemoteURL != null) ? moduleFederationRemoteURL : "" %>'
			)
	);
</aui:script>