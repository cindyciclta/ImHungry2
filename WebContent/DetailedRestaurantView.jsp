<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Restaurant Info - I'm Hungry</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<style>
	    body {
	        background-color: #f5f5f5; /* white smoke background */
	    }
	    #navigationSide{
	    	display: None;
	    }
	    #wrapper {
	        margin-top: 20px !important;
	    }
	    .description {
	        margin-left:2em;
	    }
	    .title {
	        margin-top: 1em;
	    }
	    @media print {
		  body {
		    color: #000;
		  }
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
	</style>
</head>
<body>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<% String term = (String) request.getAttribute("term"); %>
	<script>
		function printPage(){
			window.print();
		}
	
		function backToResults(link){
			window.location = link;		
		}
		
		function navigateTo(link){
			window.location = link;
		}
		
		function addToList(index, item, type){
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			if(list != ""){
				var xhr = new XMLHttpRequest();
				var searchterm = "<%= term%>";
				var trimmed = searchterm.replace(" ", "_");
				xhr.open("GET", "/ImHungry/RedirectionController?action=addtolist&term="+trimmed +"&index=" + index + "&list=" + list + "&item=" + item + "&type=" + type, true);
				xhr.send();
			}
		}	
	</script>
	
	<%
	Map<String, String> fields = ((Map<String, String>)request.getAttribute("response"));
	int index = (int)request.getAttribute("index");
	int item = (int)request.getAttribute("item");
	String link = fields.get("imageUrl");
	
	%>
	
    <div id="wrapper" class="container">
        <div class="row">
            <div class="col-md-8">
                <div class="row">
                    <div class="col-md-12">
                        <div class="container">
                            <h1 class="display-4"><%=fields.get("name")%></h1>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-10">
                        <div class="container">
                            <h5 class="title">Address</h5>
                            <h7 class="description" onclick=<%="navigateTo(" + "\"" + fields.get("mapsLink") + "\"" + ")"%>><%=fields.get("address")%></h7>
                            <h5 class="title">Phone Number</h5>
                            <h7 class="description"><%=fields.get("phoneNumber")%></h7>
                            <h5 class="title">Website</h5>
                            <h7 class="description" onclick=<%="navigateTo(" + "\"" + fields.get("websiteLink") + "\"" + ")"%>>Link to page</h7>
                        </div>
                    </div>
                </div>
            </div>
            
   <%--          <div class="col-md-auto order-1" id="navigationSide">
                <div id="sidebar-wrapper" class="navbar navbar-light">
                    <div class="navbar-nav" >
                        <ul class="navbar-nav">
                            <li class="nav-item">
                                <a class="btn btn-secondary" onclick="printPage()">Printable Version</a>
                            </li>
                            <li class="nav-item my-3">
                                <a class="btn btn-secondary" onclick=<%="backToResults(" + "\"" + "/ImHungry/RedirectionController?action=results&term="+term +"&index=" + index + "\""  + ")"%>>Return to Results</a>
                            </li>
                            <li class="nav-item dropdown">
                                <div class="form-group">
                                    <select required class="form-control" id="managelist">
                                        <option value=""><!-- None --></option>
                                        <option value="favorites">Favorites</option>
                                        <option value="toexplore">To Explore</option>
                                        <option value="donotshow">Do Not Show</option>
                                    </select>
                                </div>
                            </li>
                            <li class="nav-item">
		                       <input class="btn btn-secondary" onclick=<%="addToList("+term +"," + index + "," + item + "," + "\"restaurant\"" + ")"%> type="button" value="Add to List">
		                    </li>
                        </ul>
                    </div>
                </div>
            </div> --%>
            <div class="col-md-auto order-1">
		       <div id="sidebar-wrapper" class="navbar navbar-light">
		           <div class="navbar-nav" >
		               <ul class="navbar-nav">
		                   <li class="nav-item dropdown">
		                       <div class="form-group">
		                           <select required class="form-control" id="managelistselect">
		                               <option value=""><!-- None --></option>
		                               <option value="favorites">Favorites</option>
		                               <option value="toexplore">To Explore</option>
		                               <option value="donotshow">Do Not Show</option>
		                           </select>
		                       </div>
		                   </li>
		                   <li class="nav-item">
		                       <input class="btn btn-secondary" onclick=<%="addToList("+ index + "," + item + "," + "\"restaurant\"" + ")"%> type="button" value="Add to List">
		                   </li>
		                   <li class="nav-item my-3">
		                       <a class="btn btn-secondary" onclick=<%="backToResults(\"" + "/ImHungry/RedirectionController?action=results&term="+term +"&index=" + index + "\")"%>>Back to Results</a>
		                   </li>
		                   <li class="nav-item">
                                <a class="btn btn-secondary" onclick="printPage()">Printable Version</a>
                            </li>
		               </ul>
		           </div>
		       	</div>
		      </div>
        </div>
    </div>
</body>
</html>