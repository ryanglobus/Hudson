	<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="mainRegister"/>
		<title>Register</title>
		<script>
		jQuery(function() {
			var $ = jQuery

			$('[data-toggle=tooltip]').tooltip()
		})
		</script>
	</head>
	
	<body>
		${flash.message}
		<h2 class="text-center">Register an Account</h2>
		<br>
		<g:form class="form-horizontal" action="newusersession" useToken="true" role="form">
			<% def fields = [['firstName', 'First Name', 'text'],
							 ['lastName', 'Last Name', 'text'],
							 ['email', 'Email', 'email'],
							 ['phone', 'Phone', 'tel'],
							 ['password', 'Password', 'password'],
							 ['confirmPassword', 'Confirm Password', 'password']] %>
			<% for (def field : fields) { %>
			
			<div class="form-group">
				<%if (field[0] == "phone") { %>
					<label for="${field[0]}" class="col-sm-offset-3 col-sm-2 control-label">${field[1]} (Optional):</label>
					<span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" data-placement="top" title="Include your phone number to receive SMS notifications"></span>
				<% } else { %>
					<label for="${field[0]}" class="col-sm-offset-3 col-sm-2 control-label">${field[1]}</label>
				<% } %>
				<div class="col-sm-3">
					<%if (field[0] == "phone") { %>
						<g:field class="form-control" type="${field[2]}" name="${field[0]}" placeholder="${field[1]} (Optional)"/>
					<% } else { %>
						<g:field class="form-control" type="${field[2]}" name="${field[0]}" required="true" placeholder="${field[1]}"/>
					<% } %>
				</div>
			</div>
			<% } %>
			<div class="form-group">
				<label for="frequency" class="col-sm-offset-3 col-sm-2 control-label">Notification Frequency:</label>
				<div class="col-sm-3">
					<select class="form-control" name="frequency" id="frequency">
						<option value="30" selected="selected">30 Minutes</option>
						<option value="60">1 Hour</option>
						<option value="120">2 Hours</option>
						<option value="240">4 Hours</option>
						<option value="1440">1 Day</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="carrier" class="col-sm-offset-3 col-sm-2 control-label">Carrier:</label>
				<div class="col-sm-3">
					<select class="form-control" name="carrier" id="carrier">
						<option value="ATT" selected="selected">AT&T</option>
						<option value="TMOBILE">T-Mobile</option>
						<option value="VIRGIN">Virgin</option>
						<option value="CINGULAR">Cingular</option>
						<option value="SPRINT">Sprint</option>
						<option value="VERIZON">Verizon</option>
						<option value="NEXTEL">Nextel</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div style="text-align:center">
					<g:field class="btn btn-default" name="submit" type="submit" value="Submit"/>
				</div>
			</div>
			
		</g:form>
	</body>
</html>
