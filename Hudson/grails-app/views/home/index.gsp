<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main"/>
		<title>Login</title>
	</head>
	
	<body>
		<div id="carousel" class="carousel slide" data-ride="carousel">
  			<!-- Indicators -->
  			<ol class="carousel-indicators">
    			<li data-target="#carousel" data-slide-to="0" class="active"></li>
    			<li data-target="#carousel" data-slide-to="1"></li>
    			<li data-target="#carousel" data-slide-to="2"></li>
  			</ol>

  			<!-- Wrapper for slides -->
  			<div class="carousel-inner">
    			<div class="item active">
      				<img src="/Hudson/images/sanfran.jpg" alt="Much apartments!" width="100%">
      				<div class="carousel-caption">
        				<h2>Welcome To Hudson</h2>
        				<p>Your One Stop Shop For All Of Your Housing Needs!</p>
      				</div>
    			</div>
    			<div class="item">
      				<img src="/Hudson/images/houses.jpg" alt="Such space" width="100%">
      				<div class="carousel-caption">
        				<h2>Search All Of Your Favorite Neighborhoods</h2>
        				<p>From the Mission, to Palo Alto, to Santa Cruz. Hudson's got you covered.</p>
      				</div>
    			</div>
    			<div class="item">
      				<img src="/Hudson/images/bridge.jpg" alt="Wow" width="100%">
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
				<label for="username" class="col-sm-offset-3 col-sm-2 control-label">Email:</label>
				<div class="col-sm-3">
					<g:field class="form-control" type= "email" name= "username" required="true" placeholder="Email"/>
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-sm-offset-3 col-sm-2 control-label">Password:</label>
				<div class="col-sm-3">
					<g:field class="form-control" type= "password" name= "password" required="true" placeholder="Password"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-5 col-sm-4">
					<g:field class="btn btn-default" name="submit" type= "submit" value="Login"/>
				</div>
			</div>
		</g:form>

		<p class="text-center">
			<g:link action= "register">Don't have an account? Register for Hudson!</g:link>
		</p>
		<p class="text-center">
			<g:link action ="forgotPassword">Forgot password? Click here to reset</g:link>
		</p>
			
	</body>

</html>