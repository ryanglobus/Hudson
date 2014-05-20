<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Profile Settings</title>
	</head>
	
	<body>
	${flash.message }
		<g:form class="form-horizontal" action="changePassword" useToken="true" role="form">
			<div class="form-group">
				<label for="oldPassword" class="col-sm-2 control-label">Old Password:</label>
				<div class="col-sm-3">
					<g:field type="password" name="oldPassword" class="form-control" placeholder="Old Password"/>
				</div>
			</div>
			<div class="form-group">
				<label for="newPassword" class="col-sm-2 control-label">New Password:</label>
				<div class="col-sm-3">
					<g:field type="password" name="newPassword" class="form-control" placeholder="New Password"/>
				</div>
			</div>
			<div class="form-group">
				<label for="confirmPassword" class="col-sm-2 control-label">Confirm Password:</label>
				<div class="col-sm-3">
					<g:field type="password" name="confirmPassword" class="form-control" placeholder="Confirm Password"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-4">
					<g:field class="btn btn-default" name="submit" type="submit" value="Submit"/>
				</div>
			</div>
		</g:form>
		
	</body>
</html>