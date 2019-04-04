<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Vector"%>
<%@page import="models.SearchTermModel"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="
https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
crossorigin="anonymous">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<title>Search - I'm Hungry</title>
<style>

.img-container {
	position: relative;
	display: inline-block;
}

.img-container .overlay {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 1;
}

.overlay span {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, 50%);
}

#feed-me {
	color: blue;
}

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

p7 {
	font-size: 25px;
}
h1 {
	font-size: 60px;

}
</style>
</head>


<body>
	<script>
		function redirectToResults(){
			
			document.getElementById("emojiImage").src = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/155/grinning-face_1f600.png";
			
			setTimeout(function(){
				var query = document.getElementById("termInput").value;
				var limit = document.getElementById("limitInput").value;
				var trimmed = query.replace(" ", "_");
				window.location = '/ImHungry/SearchPageController?action=search&term='+encodeURIComponent(trimmed) + "&limit=" + limit + "&rest_page=1" + "&reci_page=1";
			}, 1000);
		}
	</script>
	<div class="col-md-auto order-1" style="float: right">
    	<div id="sidebar-wrapper" class="navbar navbar-light">
     		<div class="navbar-nav" >
           		<ul class="navbar-nav">
					<li class="nav-item">
                    	<a id="signOutLink" class="btn btn-secondary" onclick="window.location = '/ImHungry/SignInView.jsp';">Sign Out</a>
                    </li>
                </ul>
            </div>
		</div>
	</div>
	<div class="container mt-5 h-100">
		<div class="h-100 row justify-content-center align-items-center">
			<div class="container">
			  <form>
				  <div class="container">
				  	<div class="row justify-content-center align-items-center">
				  		<h1>I'm Hungry</h1>
				 	 </div>
				  </div>
			  	<br>
			  	<br>

			  	<div class="container" id="inputFields">
			  		<div class="row justify-content-center align-items-center">
			  			<div class="form-row">
			  				<div id="searchBar" class="form-group col-md-7">
			  					<input class="form-control mr-sm-2" type="search" id="termInput" placeholder="Enter food" aria-label="Search" autofocus>
			  				</div>
			  				<div id="numResults" class="form-group col-md-2">
			  					<input class="form-control mr-sm-2" type="number" min="1" value="5" id="limitInput" title="Number of items to show in results">
			  				</div>
			  				<div id="searchRadius" class="form-group col-md-3">
			  					<input class="form-control mr-sm-2" type="number" min="1" value="5" placeholder="radius" id="radiusInput">
			  				</div>
			  			</div>
			  		</div>
			  </div>

<%
	String token = (String)request.getAttribute("token");
%>

<script>
	function redirectToResults(){
		
		document.getElementById("emojiImage").src = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/155/grinning-face_1f600.png";
		
		setTimeout(function(){
			var query = document.getElementById("termInput").value;
			var limit = document.getElementById("limitInput").value;
			var radius = document.getElementById("radiusInput").value;
			var trimmed = query.replace(" ", "_");
      window.location = '/ImHungry/SearchPageController?action=search&term='+encodeURIComponent(trimmed) + "&token=" + <%=token %> + "&limit=" + limit + "&radius=" + radius + "&rest_page=1" + "&reci_page=1";
		}, 1000);
	}
	$(document).ready(function() {
		var obj = document.getElementById("inputFields");
		obj.addEventListener("keydown", function(e) {
			if (e.keyCode == 13) {
				redirectToResults();
			}
		});
	});
</script>

<div class="container mt-5 h-100">
	<div class="h-100 row justify-content-center align-items-center">
		<div class="container">
		  <form>
			  <div class="container">
			  	<div class="row justify-content-center align-items-center">
			  		<div class="form-group">
			  			<div id="emojiButton" class="img-container" onclick="redirectToResults()">
			  				<img id="emojiImage" src="
							https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/320/apple/155/pouting-face_1f621.png"
							class="img-fluid" alt="Responsive image">
						  		<div class="overlay">
						  			<span id="feed-me">Feed me!</span>
						  		</div>
			  				</div>
			  			</div>
			    	</div>
			  </div>
			  </form>
			  </div>
			</div>
		</div>
		<div class="container mt-5 h-100">
			<div class="h-100 row justify-content-center align-items-center">
				<div class="container">
				  	<div class="row justify-content-center align-items-center">
						<p7 id="hist">Search History</p7>
					</div>
				</div>
				<div class="container">
					<div class="row justify-content-center align-items-center">
						<table class="table-dark table-hover table-stripped table-borderless">
							<thead>
							</thead>
							<tbody>
								<%
								Vector<SearchTermModel> searches = (Vector<SearchTermModel>)request.getAttribute("searches");
								int i=1;
								for(SearchTermModel search : searches){
								%>
								<tr>
									<td>
										<p1 id=<%="searchHist" + i%>><a href=<%="\"/ImHungry/SearchPageController?action=search&term=" + search.term + "&token=" + token + "&limit=" + search.limit + "&radius=" + search.radius +  "&rest_page=1" + "&reci_page=1\""%>><%=search.term%></a></p1>
									</td>
								</tr>
								
								<%
								i++;
								}
								%>
							</tbody>
						</table>
					</div>
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
