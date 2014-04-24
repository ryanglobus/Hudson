<html>
	<head>
		<title>Register</title>
	</head>
	
	<body>
		<g:form action="newusersession">
			<label>First name:</label> <input type="text" name="firstName"/>
			<label>Last name:</label> <input type="text" name="lastName"/>
			<label>Email:</label> <input type="email" name="email"/>
			<label>Phone:</label> <input type="text" name="phone"/>
			<label>Password:</label> <input type="text" name="password"/>
			<label>Notification Frequency (in minutes):</label> <input type="number" name="frequency"/>
			<label>&nbsp;</label><input type= "submit" value="Submit"/>
		</g:form>
		
	</body>
</html>