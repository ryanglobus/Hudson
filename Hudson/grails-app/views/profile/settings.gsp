<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-multiselect.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}" type="text/css">
		<script src="${resource(dir: 'js', file: 'bootstrap-multiselect.js')}"></script>
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>Profile Settings</title>
	</head>
	
	<body>
	<b>${flash.message }</b>
	<h3>Reset Password</h3>
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
	
	<h3>Edit Query</h3>
	<h4>Select a query to edit:</h4>
	<div class="panel-group" id="accordion">
  		<g:each var="qry" in="${queries}">
  		<g:if test="${qry.isCancelled == false}">
  		<div class="panel panel-default">
    		<div class="panel-heading">
      			<h4 class="panel-title">
        			<a data-toggle="collapse" data-parent="#accordion" href="#${qry.id}">
          				${qry.name}
        			</a>
      			</h4>
    		</div>
    		<div id="${qry.id}" class="panel-collapse collapse">
      			<div class="panel-body">
      				<g:render template="/shared/queryFormTemplate" model="['isNew': false, 'query': qry]" />
      			</div>
    		</div>
  		</div>
  		</g:if>
  		</g:each>
	</div>
	
	</body>
</html>