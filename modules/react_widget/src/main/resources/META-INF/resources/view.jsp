<%@ include file="/init.jsp" %>

<div id="<portlet:namespace />-root"></div>

<aui:script>
	import(
		Liferay.ThemeDisplay.getPathContext() + '/o/react_widget/js/index.js'
	).then(
		(module) => module.default('<portlet:namespace />-root')
	);
</aui:script>