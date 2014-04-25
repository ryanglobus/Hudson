<html>
	<head>
		<title>Query Saved</title>
	</head>
	
	<body>
		<p>Your query has been logged. Hudson will monitor Craigslist for your housing preferences.</p>
		<g:link controller="profile">Create a new search</g:link>
		
		<p>TESTING INFORMATION: user and query information displayed below</p>
		User ${usr.firstName} ${usr.lastName} ${usr.passwordHash}
		
		Query ${query.searchText} ${query.minRent } ${query.maxRent }
		
	</body>
</html>