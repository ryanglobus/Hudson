<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}", type="text/css">
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>Query Created!</title>
	</head>
	<body>
		<h1>Query Successfully Created</h1>
		
		<h4>Query Name: ${query.name}</h4>
		Search Text: ${query.searchText} <br>
		Min Rent: ${query.minRent} <br>
		Max Rent: ${query.maxRent} <br>
		Number of Bedrooms: ${query.numBedrooms} <br>
		Housing Type: ${housingType} <br>
		Cats: ${query.cat} <br>
		Dogs: ${query.dog} <br>
		Instant Reply: ${query.instantReply} <br>
		<g:if test="${query.instantReply}"> 
			 Reply Message: ${query.responseMessage} <br>
		</g:if>
	
		<g:link controller="profile">Create a new search</g:link> <br>
		<g:link controller="home" action="homepage">Or Go Home!</g:link> <br>
	</body>
	
</html>