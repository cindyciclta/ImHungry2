<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="
	https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
	<title>Sign In - I'm Hungry</title>
	<style>
		body, html {
			height: 100%;
			font-family: "Comic Sans MS", cursive, sans-serif;
			background: linear-gradient(183deg, #ffc1f3, #a1e7e6, #dabdf8, #bdf8c3, #f1f698, #fad591);
			background-size: 1200% 1200%;

			-webkit-animation: rainbow 21s ease infinite;
			-moz-animation: rainbow 21s ease infinite;
			animation: rainbow 21s ease infinite;
		}
		@-webkit-keyframes rainbow {
		    0%{background-position:50% 0%}
		    50%{background-position:51% 100%}
		    100%{background-position:50% 0%}
		}
		@-moz-keyframes rainbow {
		    0%{background-position:50% 0%}
		    50%{background-position:51% 100%}
		    100%{background-position:50% 0%}
		}
		@keyframes rainbow { 
		    0%{background-position:50% 0%}
		    50%{background-position:51% 100%}
		    100%{background-position:50% 0%}
		}
	</style>
</head>

<body>

<div class="container mt-5 h-100">
	<div class="h-100 row justify-content-center align-items-center">
		<div class="container">
		  <form method="post" action="/ImHungry/SignInController">
			  <div class="container">
			  	<div class="row justify-content-center align-items-center">
			  		<h1>Sign In</h1>
			 	 </div>
			  </div>
		  	<br>
		  	<br>
		  	
		  	<div class="container">
		  		<div class="row justify-content-center align-items-center">
		  			<div class="form-row">
		  				<div id="searchBar" class="form-group col-md-12">
		  					<input class="form-control mr-sm-2" type="text" id="username" name="username" placeholder="Username...">
		  				</div>
		  			</div>
		  		</div>
		   </div>
		   
		   <div class="container">
		  		<div class="row justify-content-center align-items-center">
		  			<div class="form-row">
		  				<div id="searchBar" class="form-group col-md-12">
		  					<input class="form-control mr-sm-2" type="password" id="password" name="password" placeholder="Password...">
		  				</div>
		  			</div>
		  		</div>
		   </div>
		   
		   <div class="container">
		  		<div class="row justify-content-center align-items-center">
		  			<div class="form-row">
		  				<div id="searchBar" class="form-group col-md-12">
		  					<input id="signInButton" class="form-control mr-sm-2" type="submit"/>
		  				</div>
		  			</div>
		  		</div>
		   </div>
		   
		   <div class="container">
		  		<div class="row justify-content-center align-items-center">
		  			<div class="form-row">
		  				<div id="searchBar" class="form-group col-md-12">
		  					<h3><a href="/ImHungry/SignUpView.jsp" id="signUpLink">Sign Up Here</a></h3>
		  				</div>
		  			</div>
		  		</div>
		   </div>
		   
		  </form>
		  </div>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
	<script src="
	https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous"></script>
	<script src="
	https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous"></script>
</body>
</html>
