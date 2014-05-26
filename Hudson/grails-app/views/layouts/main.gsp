<%@ page import="hudson.User" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<!--  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> -->
		
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet"
			href="${resource(dir: 'bootstrap-3.1.1-dist/css', file: 'bootstrap.min.css')}"
			type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
		
		<!--  Google maps -->
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}" , type="text/css">
		<style type="text/css">
			html {
				height: 100%
			}

			body {
				height: 100%;
				margin: 0;
				padding: 0
			}

			#map-canvas {
				height: 100%
			}
		</style>
		
		<script src="${resource(dir: 'js', file: 'jquery-2.1.0.min.js')}"></script>
		<script src="${resource(dir: 'bootstrap-3.1.1-dist/js', file: 'bootstrap.min.js')}"></script>
		<g:layoutHead/>
		<g:javascript library="application"/>		
		<r:layoutResources />
	</head>
	<body>
		<nav class="navbar navbar-inverse" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle"
						data-toggle="collapse" data-target="#main-navbar-body">
				    	<span class="sr-only">Toggle navigation</span>
				    	<span class="icon-bar"></span>
				    	<span class="icon-bar"></span>
				    	<span class="icon-bar"></span>
				     </button>
					<g:link class="navbar-brand" uri="/">Hudson</g:link>
				</div>
				<div class="collapse navbar-collapse" id="main-navbar-body">
					<% def user = User.findById(session["userid"]) %>
					<ul class="nav navbar-nav">
					</ul>
					<ul class="nav navbar-nav navbar-left">
						<%if (user != null) { %>
							<li><g:link controller="profile" action="index">Create Query</g:link></li>
							<li><g:link controller="profile" action="newResults" params="[queryName: "all"]">New Posts <span class="badge"><%=session["newPostCount"]%></span></g:link></li>
						<% } %>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<% if (user == null) { %>
							<li><g:link controller="home" action="index">Log In</g:link></li>
						<% } else { %>
							<li><p class="navbar-text">Hi, ${user.firstName}</p></li>
							<li><g:link controller="home" action="logout">Log Out</g:link></li>
							<li><g:link controller="profile" action="settings">Profile Settings</g:link></li>
						<% } %>
					</ul>
				</div>
			</div>
		</nav>
		<div class="container">
			<g:layoutBody/>
		</div>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<r:layoutResources />
	</body>
</html>
