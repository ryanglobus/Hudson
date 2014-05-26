<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
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
      				<g:form useToken="true" class="form-inline query-form" action ="editQuery" role="form" id="queryForm">
						<div class="form-group">
							<b>Query Name:</b>  
							<g:field type="text" name="queryName" class="form-control" value="${qry.name}" />
						</div>
						<br>
						<div class="form-group">
							<b>Search Text:</b>  
							<g:field type="text" name="searchText" class="form-control" value="${qry.searchText}" />
						</div>
						<br>
						<div class="form-group">
							<b>Min # Rooms:</b> <g:field type="number" class="form-control" name="numRooms" value="${qry.numBedrooms }" />
						</div>
						<br>
						<label class="control-label">Rent:</label>
						<div class="form-group">
							<b>$</b> <g:field type="number" class="form-control" name="minrent" value="${qry.minRent}" />
						</div>
						<label class="control-label">to</label>
						<div class="form-group">
							<b>$</b> <g:field type="number" class="form-control" name="maxrent" value="${qry.maxRent}"/>
						</div>
						<br>
						<b>Housing Type:</b> <select class="form-control" name = "type" id="type">
							<option value = "ANY">Any Housing Type</option>
							<option value = "APARTMENT">Apartment</option>
							<option value = "CONDO">Condo</option>
							<option value = "COTTAGE_CABIN">Cottage/Cabin</option>
							<option value = "DUPLEX">Duplex</option>
							<option value = "FLAT">Flat</option>
							<option value = "HOUSE">House</option>
							<option value = "IN_LAW">In-Law</option>
							<option value = "LOFT">Loft</option>
							<option value = "TOWNHOUSE">Townhouse</option>
							<option value = "MANUFACTURED">Manufactured</option>
							<option value = "ASSISTED_LIVING">Assisted Living</option>
							<option value = "LAND">Land</option>
						</select>
						<br>
						<% def checkboxes = [['cat', 'Cat'],
											 ['dog', 'Dog']] %>
						<% for (def checkbox : checkboxes) { %>
						<div class="checkbox">
							<label>
								<g:checkBox name="${checkbox[0]}" /> ${checkbox[1]}
							</label>
						</div>
						<% } %>
						<br />
						<div class="checkbox">
							<label>
								<g:checkBox name="notify" value="${qry.notify}" /> Receive Notifications?
							</label>
						</div>

						<input type="checkbox" name="instantReply" value="${qry.instantReply}" id="instantReply"/>
						<label for="instantReply">Auto-Reply?</label>
						<br>
						<textArea form="queryForm" rows="8" cols="80" name="responseMessage" id="responseBox" style="display:none" placeholder ="Auto-reply message">${qry.responseMessage}</textArea>
      					<input type="hidden" name="qryId" value="${qry.id}">
      					<br>
      					<input class="btn btn-primary" type= "submit" value="Edit Query"/>
      				</g:form>
      			</div>
    		</div>
  		</div>
  		</g:each>
	</div>
	
	</body>
</html>