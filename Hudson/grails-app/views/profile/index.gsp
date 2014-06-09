<%@ page import="hudson.Query" %>
<%@ page import="hudson.neighborhood.*" %>

<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-multiselect.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}" type="text/css">
		<script src="${resource(dir: 'js', file: 'bootstrap-multiselect.js')}"></script>
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>New Query</title>
	</head>
	
	<body>
	${flash.message }
		<g:if test="${flash.invalidToken }">
			Invalid form token!
		</g:if>
		<div style="text-align:center" id="query-tabs">
			<h2>Start Your New Housing Search</h2>
			<div class="tab-content">
				<g:render template="/shared/queryFormTemplate" model="['isNew': true, 'query': new Query()]" />
			</div>
			<br><br>
		</div>
	</body>	
</html>
