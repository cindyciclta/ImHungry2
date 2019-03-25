<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<title>Recipe Info - I'm Hungry</title>

<style>
		@media print {
		  body {
		    color: #000;
		  }
		  #foodPic{
		  	display: None;
		  }
		}
	#wrapper {
		margin-top: 20px !important;
	}
	.title {
		margin-top: 1em;
	}
	#foodPic {
		width: 380px;
		height: 280px;
		margin-top:5px;
		margin-bottom:5px;
	}
	
	body, html {
		height: 100%;
		font-family: "Comic Sans MS", cursive, sans-serif;
	}
	
	td {
		padding: 4px !important;
	}
	/* Numbers the instructions */
	body {
		background-color: #f5f5f5;
	     		counter-reset: h7counter;
	 		}
	   h7:before {
	       content: counter(h7counter) ".\0000a0\0000a0";
	       counter-increment: h7counter;
	   }
	   ol {
	   	padding: 0 !important;
	   }
	   h {
	   	font-size: 17px !important;
	   }
</style>
		
</head>
<body class="p-3 mb-2 bg-dark text-white">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<%String term = (String) request.getAttribute("term");  %>
	<script>
	
		var list = "";
		
		function printPage(){
			window.print();
		}
	
		function backToResults(link){
			window.location = link;		
		}
		
		function addToList(index, item, type){
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			if(list != ""){
				var xhr = new XMLHttpRequest();
				var searchterm = "<%= term%>";
				var trimmed = searchterm.replace(" ", "_");
				xhr.open("GET", "/ImHungry/RedirectionController?action=addtolist&term="+ trimmed +"&index=" + index + "&list=" + list + "&item=" + item + "&type=" + type, true);
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
		        			<h1>Recipe for <%=fields.get("name")%></h1>
		        		</div>
		        		<div class="container">
		        			<img id="foodPic" src=<%=link%> class="img-fluid" alt="Responsive image">
		        		</div>
		        	</div>
	      		</div>
	      		<div class="row">
	        		<div class="col-lg-12">
	        			<div class="container">
	        				<br>
	        				<h>Prep Time: <%=fields.get("prepTime")%></h>
	        				<br>
	        				<h>Cook Time: <%=fields.get("cookTime")%></h>
        					<h5 class="title">Ingredients</h5>
        					<div class="container-fluid">
        						<!-- TODO make this table fill dynamically -->
        						<table class="table-dark table-borderless">
        							<thead>
        							</thead>
        							<tbody>
        							
        							<%
										String[] splitIngredients = fields.get("ingredients").split("SPLIT");
										
										int count = 0;
										
										for(String ing : splitIngredients){
										%>
										
										<%
											if(count % 3 == 0){
										%>
											<tr>
										<%
										}
										%>
										
											<td>
			        							<li>
			        								<h><%=count+1%>. <%=ing %><h> 
			        							</li>
        									</td>
										
										<%
											if(count % 3 == 2){
										%>
											</tr>
										<%
										}
										%>
										
										<%
											count += 1;
										} %>
        							</tbody>
        						</table>
        					</div>
        					<h5 class="title">Instructions</h5>
        					<div class="container-fluid">
        						<!-- TODO make this table fill dynamically -->
        						<table class="table-dark table-borderless">
        							<thead>
        							</thead>
        							<tbody>
        							<%
									String[] splitInstructions = fields.get("instructions").split("SPLIT");
									
									for(String ins : splitInstructions){
									%>
										<tr>
        									<td>
			        							<ol>
			        								<h7><%=ins%></h7> 
			        							</ol>
        									</td>
        								</tr>
									<%}%>
        							</tbody>
        						</table>
        					</div>
	        			</div>
	        		</div>
	      		</div>
	    	</div>
	    	
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
		            
		                       <input class="btn btn-secondary" onclick=<%="addToList("+ index + "," + item + "," + "\"recipe\"" + ")"%> type="button" value="Add to List">
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