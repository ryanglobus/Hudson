<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="mainHome"/>
		<title>Login</title>
	</head>
	
	<body>
		<div id="carousel" class="carousel slide" data-ride="carousel" style="width:100%">
  			<!-- Indicators -->
  			<ol class="carousel-indicators">
    			<li data-target="#carousel" data-slide-to="0" class="active"></li>
    			<li data-target="#carousel" data-slide-to="1"></li>
    			<li data-target="#carousel" data-slide-to="2"></li>
  			</ol>

  			<!-- Wrapper for slides -->
  			<div class="carousel-inner">
    			<div class="item active">
      				<img src="${resource(dir: 'images', file:'sanfran.jpg')}" alt="Much apartments!" width="100%">
      				<div class="carousel-caption">
        				<h2>Welcome To Hudson</h2>
        				<p>Your One-Stop Shop For All Of Your Housing Needs!</p>
      				</div>
    			</div>
    			<div class="item">
      				<img src="${resource(dir: 'images', file:'houses.jpg')}" alt="Such space" width="100%">
      				<div class="carousel-caption">
        				<h2>Search All Of Your Favorite Neighborhoods</h2>
        				<p>From the Mission, to Palo Alto, to Santa Cruz. Hudson's got you covered.</p>
      				</div>
    			</div>
    			<div class="item">
      				<img src="${resource(dir: 'images', file:'bridge.jpg')}" alt="Wow" width="100%">
      				<div class="carousel-caption">
        				<h2>Live Your Life!</h2>
        				<p>Create a query and let Hudson do the rest. Housing results in real time, without the stress.</p>
      				</div>
    			</div>
  			</div>

  			<!-- Controls -->
  			<a class="left carousel-control" href="#carousel" data-slide="prev">
    			<span class="glyphicon glyphicon-chevron-left"></span>
  			</a>
  			<a class="right carousel-control" href="#carousel" data-slide="next">
    			<span class="glyphicon glyphicon-chevron-right"></span>
  			</a>
		</div>
		
	
		${flash.message}

		
		<h2 class="text-center">Log In</h2>

		<g:form class="form-horizontal" controller="home" action = "login" useToken="true" role="form">
			<%-- TODO: useToken="true" on ALL forms to prevent CSRF AND in controllers --%>
			<div class="form-group">
				<div class="col-sm-4 col-md-offset-4">
					<g:field class="form-control" type= "email" name= "username" required="true" placeholder="Email" style="background-color: #fff !important"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-4 col-md-offset-4">
					<g:field class="form-control" type= "password" name= "password" required="true" placeholder="Password" style="background-color: #fff !important"/>
				</div>
			</div>
			<div class="form-group">
				<div style="text-align:center">
					<g:field class="btn btn-default" name="submit" type= "submit" value="Login"/>
				</div>
			</div>
		</g:form>
		
		<hr>

		<p class="text-center">
			<g:link action= "register" style="color: #999">Don't have an account? Click here to register!</g:link>
		</p>
		<p class="text-center">
			<g:link action ="forgotPassword" style="color: #999">Forgot password? Reset here</g:link>
		</p>
			
	</body>

</html>