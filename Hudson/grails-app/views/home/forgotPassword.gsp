<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<title>Password Reset</title>
	</head>
	
	<body>
		${flash.message}
		
		<g:form class="form-horizontal" controller="home" action = "sendPassword" useToken="true" role="form">
			<div class="form-group">
				<label for="username" class="col-sm-offset-3 col-sm-2 control-label">Email:</label>
				<div class="col-sm-3">
					<g:field class="form-control" type= "email" name= "username" required="true" placeholder="Email"/>
				</div>
			</div>
		
			<div class="form-group">
				<div class="col-sm-offset-5 col-sm-4">
					<g:field class="btn btn-default" name="submit" type= "submit" value="Send Password"/>
				</div>
			</div>
		</g:form>	
	</body>
</html>