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
			<h2 style="text-align:center">Create a Query</h2>
			<br>
			<div class="tab-content">
					<g:form useToken="true" class="query-form form-horizontal" action = "newquery" role="form" id="queryForm">

						<div class="category">
							<h4>Location</h4>
							<div class="form-group">
								<label for="region" class="col-sm-2 control-label">Region:</label>
								<div class="col-sm-9">
									<select class="form-control col-sm-9" name="region">
										<% Region region = Region.sfbay() %>
										<option value="${region.value}">${titlize(region.name)}</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="city" class="col-sm-2 control-label">City:</label>
								<div class="col-sm-9">
									<select class="form-control" name="city" id="city">
										<option value="">All cities</option>
										<% for (City city : region.cities) { %>
											<option value="${city.value}">${titlize(city.name)}</option>
										<% } %>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="neighborhoods" class="col-sm-2 control-label">Neighborhoods:</label>
								<div class="col-sm-9">
									<select class="form-control multiselect" name="neighborhoods" id="neighborhoods" multiple disabled>
									</select>
								</div>
							</div>
						</div>

						<br>
						<div class="category">
							<h4>Housing Preferences</h4>
							<div class="form-group">
								<label for="numRooms" class="col-sm-2 control-label"># Bedrooms:</label>
								<div class="col-sm-9"> 
									<g:field type="number" class="form-control" name="numRooms" placeholder="# BRs" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2">Rent:</label>
								<div class="col-sm-9">
									<label for="minrent" class="sr-only control-label">Minimum:</label>
									<g:field type="number" class="form-control col-sm-3" name="minrent" placeholder="Minimum" />
									<label class="control-label col-sm-1">to</label>
									<label for="maxrent" class="sr-only control-label">Maximum:</label>
									<g:field type="number" class="form-control col-sm-3" name="maxrent" placeholder="Maximum"/>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2" for="type">Type:</label>
								<div class="col-sm-9">
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
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2">Pets?</label>
								<div class="col-sm-9">
									<% def checkboxes = [['cat', 'Cats'],
												 		['dog', 'Dogs']] %>
									<% for (def checkbox : checkboxes) { %>
										<g:checkBox name="${checkbox[0]}" />
										<label for="${checkbox[0]}">${checkbox[1]}</label>
									<% } %>
								</div>
							</div>
							<div class="form-group">
								<label for="searchText" class="col-sm-2 control-label">Keywords:</label>
								<div class="col-sm-9">
									<g:field type="text" name="searchText" class="form-control" placeholder="Search" />
								</div>
							</div>
						</div>

						<br>
						<div class="category">
							<h4 class="center-block">General</h4>
							<div class="form-group">
								<label for="queryName" class="control-label col-sm-2">Query Name:</label>
								<div class="col-sm-9">
									<g:field type="text" name="queryName" class="form-control" placeholder="Name" />
								</div>
							</div>
							
							<% checkboxes = [['notify', 'Receive Notifications?', true],
											['instantReply', 'Auto-Reply', false]] %>
							<% for (def checkbox : checkboxes) { %>
								<div class="form-group">
									<label class="control-label col-sm-3" for="${checkbox[0]}">${checkbox[1]}</label>
									<div class="col-sm-9">
										<g:checkBox name="${checkbox[0]}" class="form-control" value="${checkbox[2]}" />
									</div>
								</div>
							<% } %>

							<textArea class="col-sm-offset-3" form="queryForm" rows="8" cols="60" name="responseMessage" id="responseBox" style="display:none" placeholder ="Insert a Response Message Here!"></textArea>
						</div>
						<input class="btn btn-primary center-block" type= "submit" value="Create Query"/>
					</g:form>
				<!-- </div> -->
				<br>
			</div>
			<br><br>
		</div>
	</body>	
</html>