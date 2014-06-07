<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'profile.css')}", type="text/css">
		<script src="${resource(dir: 'js', file: 'profile.js')}"></script>
		<title>New Results</title>
	</head>
	
	<body>
		<div id="map-canvas" style="width: 74%; height: 43%; position: absolute; margin-top: 143px"></div>
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAH9FBsCMDrKGcOluS-iFLpymbNT-u0go4&sensor=flase"></script>	
		<script type="text/javascript">
		
    		function initialize() {
        		var latArr = ${lats}
        		var lonArr = ${lons}
        		var test = [
        	        		<% for (String t : test) { %>
								"${t}"
							<% } %>
        	        		]
        		var str = test[0];
        		var linkArr = str.split("&quot;");
        		//alert(linkArr[2]);
        		var mapOptions = {
          			center: new google.maps.LatLng(${lat}, ${lon}),
          			zoom: 8
        		};
        		var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
				
				var infowindow = new google.maps.InfoWindow ({
					content : "",
				});
				for(var i = 0; i < latArr.length; i++) {
        			var contentString = linkArr[i*2 + 1];
        			//var html = <a 
					
        			var myLatlng = new google.maps.LatLng(latArr[i], lonArr[i]);
        			var marker = new google.maps.Marker ({
						position:myLatlng,
						map: map,
						//add in ID here to store something maybe html?
						title: contentString,
        			});
	
					google.maps.event.addListener(marker, 'click', function() {
						//def content = 
						infowindow.setContent('<a href = "'+this.title+'">'+this.title+'</a>'); //maybe?
    			    	infowindow.open(map, this);
    				});
					//infoarr[i] = infowindow;
					//markerarr[i] = marker;
        			
				}
				
      		}
      		google.maps.event.addDomListener(window, 'load', initialize);
    	</script>
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
				<div class="panel panel-default" style="margin-top:400px">
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