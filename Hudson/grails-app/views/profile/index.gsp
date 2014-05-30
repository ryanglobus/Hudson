<%@ page import="hudson.neighborhood.*" %>
<%@ page import="java.util.StringTokenizer" %>

<%
def titlize = { String str ->
	String title = ''
	StringTokenizer tokenizer = new StringTokenizer(str)
	boolean first = true
	while (tokenizer.hasMoreTokens()) {
		String token = tokenizer.nextToken()
		if (first) first = false
		else title += ' '
		title += token.capitalize()
	}
	return title
}
%>

<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-multiselect.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}" type="text/css">
		<script src="${resource(dir: 'js', file: 'bootstrap-multiselect.js')}"></script>
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>New Query</title>
	</head>
	
	<body>
	${flash.message }
		<g:if test="${flash.invalidToken }">
			Invalid form token!
		</g:if>
		<div class="col-sm-offset-2 col-sm-8" id="query-tabs">
			<ul class="nav nav-tabs">
				<li><a href="#" id="new-tab">+</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="search1">
					<g:form useToken="true" class="form-inline query-form" action = "newquery" role="form">
						<div class="form-group">
							<label for="queryName" class="sr-only control-label">Name:</label>
							<g:field type="text" name="queryName" class="form-control" placeholder="Name" />
						</div>
						
						<div class="form-group">
							<label for="searchText" class="sr-only control-label">Search:</label>
							<g:field type="text" name="searchText" class="form-control" placeholder="Search" />
						</div>
						<div class="form-group">
							<g:field type="number" class="form-control" name="numRooms" placeholder="# BRs" />
						</div>
						<br />
						<div class="form-group">
							<select class="form-control" name="region">
								<% Region region = Region.sfbay() %>
								<option value="${region.value}">${titlize(region.name)}</option>
							</select>
						</div>
						<div class="form-group">
							<select class="form-control" name="city" id="city">
								<option value="">All cities</option>
								<% for (City city : region.cities) { %>
									<option value="${city.value}">${titlize(city.name)}</option>
								<% } %>
							</select>
						</div>
						<div class="form-group">
							<label for="neighborhoods" class="sr-only control-label">Neighborhood:</label>
							<select class="form-control multiselect" name="neighborhoods" id="neighborhoods" multiple disabled>
							</select>
						</div>
						<br />
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
								<g:checkBox name="notify" value="true" /> Receive Notifications?
							</label>
						</div>

						<input type="checkbox" name="instantReply" id="instantReply"/>
						<label for="instantReply">Auto-Reply?</label>
						<input type = "text" name="responseMessage" placeholder = "Auto-reply message">
							
						<input class="btn btn-primary" type= "submit" value="Create Query"/>
					</g:form>
				</div>
			</div>
		</div>
	</body>	
</html>