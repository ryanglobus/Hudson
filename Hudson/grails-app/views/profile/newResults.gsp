<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}", type="text/css">
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>New Results</title>
	</head>
	
	<body>
		<iframe
  			width="600"
 		 	height="450"
  			frameborder="0" style="border:0"
  			src="https://www.google.com/maps/embed/v1/place?key=AIzaSyB2Uf9nK2gPGQ7z-1x_rR4-96psSngZL-8
  			&q=Starbucks+Coffee,Seattle+WA">
		</iframe>
		<h1>Query Results For ${queryTitle}</h1>
		<g:form action="deletePosts">
		<%
			def queryTitleTemp = ""
			if (queryTitle == "All Queries") {
				queryTitleTemp = "all"
			}
		%>
		<input type="hidden" name="queryName" value="${queryTitleTemp}">
		<div class="btn-group">
  			<button type="button" class="btn btn-default">${queryTitle}</button>
  			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
    			<span class="caret"></span>
    			<span class="sr-only">Toggle Dropdown</span>
  			</button>
  			<ul class="dropdown-menu" role="menu">
  				<li><g:link controller="profile" action="newResults" params="[queryName: 'all']">All</g:link></li>
    			<li class="divider"></li>
    			<g:each var="queryName" in="${queryNames}">
    				<li><g:link controller="profile" action="newResults" params="[queryName: queryName]">${queryName}</g:link></li>
    			</g:each>
  			</ul>
		</div>
		<button type="submit" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Delete Selected Posts</button>
		<br>
		<br>
		<br>
		<g:if test="${results.size() == 0}">
			<br>
			<p>Looks like you have new posts at this time, but we'll keep looking!</p>
		</g:if>
		<g:else>
			<g:each var="postList" in="${results}">				
				<div class="panel panel-default">
  						<!-- Default panel contents -->
  						<div class="panel-heading"><h3>Results For ${postList.key}   
  						<button type="button" class="btn btn-danger"><g:link action="deleteQuery" params="[queryName: postList.key]"><span class="glyphicon glyphicon-trash"></span> Delete Query</g:link></button>
  						</h3></div>
  						<!-- Table -->
  						<table class="table table-striped table-hover">
							<tr>
								<th>Delete</th>
								<th>Post Title</th>
								<th>Post Link</th>
								<th>Responded?</th>
							</tr>
							<g:each var="post" in="${postList.value}">
								<tr>
									<td><input type="checkbox" name="delete" value="${post.id}"></td>
									<td>${post.title}</td>
									<td><a href="${post.link}">${post.link}</a></td>
									<g:if test="${post.responseSent == true}">
										<td>
											<button type="button" class="btn btn-success btn-sm">
  												<span class="glyphicon glyphicon-ok"></span> Yes	
											</button>
										</td>
									</g:if>
									<g:else>
										<td>
											<button type="button" class="btn btn-danger btn-sm">
  												<span class="glyphicon glyphicon-remove"></span> No	
											</button>
										</td>
									</g:else>
								</tr>
							</g:each>
					 </table>
				</div>
				<br>
			</g:each>
		</g:else>
		</g:form>
	</body>
</html>