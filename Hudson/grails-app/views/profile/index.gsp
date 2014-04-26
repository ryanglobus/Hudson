<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}", type="text/css">
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>New Query</title>
	</head>
	
	<body>
		<div class="col-sm-offset-2 col-sm-8" id="query-tabs">
			<ul class="nav nav-tabs">
				<li class="active"><a href="#search1" data-toggle="tab">Search 1</a></li>
				<li><a href="#" id="new-tab">+</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="search1">
					<g:form class="form-inline query-form" action = "newquery" useToken="true" role="form">
						<div class="form-group">
							<label for="searchText" class="sr-only control-label">Search:</label>
							<g:field type="text" name="searchText" class="form-control" placeholder="Search" />
						</div>
						<div class="form-group">
							<g:field type="number" class="form-control" name="numRooms" placeholder="# BRs" />
						</div>
						<label class="control-label">Rent:</label>
						<div class="form-group">
							<label for="minrent" class="sr-only control-label">Minimum:</label>
							<g:field type="number" class="form-control" name="minrent" placeholder="Minimum" />
						</div>
						<label class="control-label">to</label>
						<div class="form-group">
							<label for="maxrent" class="sr-only control-label">Maximum:</label>
							<g:field type="number" class="form-control" name="maxrent" placeholder="Maximum"/>
						</div>
						<br />
						<select class="form-control" name = "type" id="type">
							<option value = "any housing type">Any Type</option>
							<option value = "apartment">Apartment</option>
							<option value = "condo">Condo</option>
							<option value = "cottage/cabin">Cottage/Cabin</option>
							<option value = "duplex">Duplex</option>
							<option value = "flat">Flat</option>
							<option value = "house">House</option>
							<option value = "in-law">In-Law</option>
							<option value = "loft">Loft</option>
							<option value = "townhouse">Townhouse</option>
							<option value = "manufactured">Manufactured</option>
							<option value = "assisted">Assisted Living</option>
							<option value = "land">Land</option>
						</select>
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
								<g:checkBox name="notify" /> Receive Notifications?
							</label>
						</div>
						<input type="checkbox" name="instantReply" id="instantReply"/>
						<label for="instantReply">Auto-Reply?</label>
						<input type = "text" name="responseMessage">
							
						<input class="btn btn-primary" type= "submit" value="newquery"/>
					</g:form>
				</div>
			</div>
		</div>
	</body>	
</html>