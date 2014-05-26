<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}", type="text/css">
		<script src="${resource(dir: 'js', file: 'resultsPage.js')}"></script>
		<g:if test="${favorites == false}">
			<title>New Results</title>
		</g:if>
		<g:else>
			<title>Favorites</title>
		</g:else>
	</head>
	
	<body>
		<g:if test="${favorites == false}">
			<h1>Query Results For ${queryTitle}</h1>
		</g:if>
		<g:else>
			<h1>Favorite Results For ${queryTitle}</h1>
		</g:else>
		
		<g:form action="deletePosts">
		<%
			def queryTitleTemp = queryTitle
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
  				<li><g:link controller="profile" action="newResults" params="[queryName: 'all', favorites:false]">All</g:link></li>
    			<li class="divider"></li>
    			<g:each var="query" in="${queries}">
    				<li><g:link controller="profile" action="newResults" params="[queryName: query.name, favorites:false]">${query.name}</g:link></li>
    			</g:each>
  			</ul>
		</div>
		<button type="submit" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Delete Selected Posts</button>
		
		<g:if test="${favorites == false}">
			<a class="btn btn-custom" href="/Hudson/profile/newResults?queryName=${queryTitleTemp}&favorites=true"><span class="glyphicon glyphicon-star"></span> View Favorites</a>
		</g:if>
		<g:else>
			<a class="btn btn-primary" href="/Hudson/profile/newResults?queryName=${queryTitleTemp}&favorites=false">View All Posts</a>
		</g:else>
				
		<br>
		<br>
		<br>
		<g:if test="${results.size() == 0}">
			<br>
			<p>Looks like you don't have any new posts at this time, but we'll keep looking!</p>
		</g:if>
		<g:else>
			<g:each var="postList" in="${results}">				
				<div class="panel panel-default">
  						<!-- Default panel contents -->
  						<div class="panel-heading"><h3>Results For ${postList.key}   
  						<a href="/Hudson/profile/deleteQuery?queryName=${postList.key}" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Delete Query</a>
  						<a class="btn btn-success" href="/Hudson/profile/settings" ><span class="glyphicon glyphicon-wrench"></span> Edit Query</a>
  						</h3></div>
  						<!-- Table -->
  						<table class="table table-striped table-hover">
							<tr>
								<th>Delete</th>
								<th>Date</th>
								<th>Post Title</th>
								<th>Responded?</th>
								<th>Favorite</th>
							</tr>
							<g:each var="post" in="${postList.value}">
								<tr>
									<td><input type="checkbox" name="delete" value="${post.id}"></td>
									<td>${post.date.get(Calendar.MONTH)}/${post.date.get(Calendar.DAY_OF_MONTH)}/${post.date.get(Calendar.YEAR)}</td>
									<td><a href="${post.link}" target="_blank">${post.title}</a></td>
									<g:if test="${post.responseSent == true}">
										<td>
											<button class="btn btn-success btn-small response" id="${post.id}"><span class="glyphicon glyphicon-ok"></span> Yes</button>
										</td>
									</g:if>
									<g:else>
										<td>			
											<button class="btn btn-danger btn-small response" id="${post.id}"><span class="glyphicon glyphicon-remove"></span> No</button>
										</td>
									</g:else>
									<g:if test="${post.favorite == false}">
										<td>
											<button class="btn btn-default btn-sm favorite" id="${post.id}"><span class="glyphicon glyphicon-star-empty"></span></button>
										</td>
									</g:if>
									<g:else>
										<td>			
											<button class="btn btn-custom btn-sm favorite" id="${post.id}"><span class="glyphicon glyphicon-star"></span></button>
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