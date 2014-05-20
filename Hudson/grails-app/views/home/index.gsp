<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<title>Login</title>
	</head>
	
	<body>
		${flash.message}
		
		<h2 class="text-center">Log In</h2>

		<g:form class="form-horizontal" controller="home" action = "login" useToken="true" role="form">
			<%-- TODO: useToken="true" on ALL forms to prevent CSRF AND in controllers --%>
			<div class="form-group">
				<label for="username" class="col-sm-offset-3 col-sm-2 control-label">Email:</label>
				<div class="col-sm-3">
					<g:field class="form-control" type= "email" name= "username" required="true" placeholder="Email"/>
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-sm-offset-3 col-sm-2 control-label">Password:</label>
				<div class="col-sm-3">
					<g:field class="form-control" type= "password" name= "password" required="true" placeholder="Password"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-5 col-sm-4">
					<g:field class="btn btn-default" name="submit" type= "submit" value="Login"/>
				</div>
			</div>
		</g:form>

		<p class="text-center">
			<g:link action= "register">Don't have an account? Register for Hudson!</g:link>
		</p>
		<p class="text-center">
			<g:link action ="forgotPassword">Forgot password? Click here to reset</g:link>
		</p>
			
	</body>

</html>