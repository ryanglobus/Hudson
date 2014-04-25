<!DOCTYPE html>
<html>
	
	<head>
		<title>Login</title>
	</head>
	
	<body>
		${flash.message}
		
		<g:form action = "login" style="padding-left:200px"> 
			<div style = "width:220px">
				<label>Name:</label> <input type= "text" name= "username"/>
				<label>Password:</label> <input type= "password" name= "password"/>
				<label>&nbsp;</label><input type= "submit" value="Login"/>
			</div>
		</g:form>
		
		<g:link action= "register" style="padding-left:400px">Register new user</g:link>
	</body>

</html>