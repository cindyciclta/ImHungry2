<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="models.ResponseModel"%>   
<%@page import="models.CollageGenerationModel"%>   
<%@page import="models.GoogleImageRequestModel"%> 
<%@page import="java.util.Map"%>  
<%@page import="java.util.ArrayList"%> 
<%@page import="org.json.JSONArray"%> 
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <style type="text/css">
        body {
     		background-color: #f5f5f5;
        }

        #outer-wrapper {
            height: 100vh;
        }

        #collage-wrapper {
            height: 40vh;
            width: 50vw;
            position: relative;
            padding: 0;
        }

        .nested-tr {
            background-color: inherit;
        }
		
		body, html {
			height: 100%;
			font-family: "Comic Sans MS", cursive, sans-serif;
		}
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<title>Results Page - I'm Hungry</title>
</head>
<body class="p-3 mb-2 bg-dark text-white">
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

	<%
	ResponseModel rm = ((ResponseModel)request.getAttribute("response"));
	String title = rm.getSearchTerm();
	int index = (int)request.getAttribute("index");
	
	JSONArray jsArray = (JSONArray) request.getAttribute("jsonarray");
	int length = (int) request.getAttribute("length");
	String term = (String) request.getAttribute("term");
	String token = (String)request.getAttribute("token");
	String pg = (String) request.getAttribute("page");
	int lim = Integer.parseInt((String)request.getSession().getAttribute("limit"));
	int rad = Integer.parseInt((String)request.getSession().getAttribute("radius"));
	int pagenumber;
	try {
		pagenumber = Integer.parseInt(pg);
	} catch (Exception e) {
		pagenumber = 1;
	}
	%>
	
	<script>
		$( document ).ready(function() {
	        console.log( "ready!" );
	        var js = <%= jsArray %>;
	      	var size = <%= length %>;
	        for (var i = 0; i < 10; i = i+1) {
	        	if (i < size) {
		        	var string = JSON.stringify(js[i]);
		        	var refactorstring = string.substr(1).slice(0,-1);
		        	addImage(refactorstring, i);
	        	}
			}
	    });
	
	    // min = minimum rotation in degrees, max = maximum rotation in degrees
	    function randomIntFromInterval(min,max) {
	        return Math.floor(Math.random()*(max-min+1)+min);
	    }
	
	    function addImage(url, index) {
	        var destination = document.getElementById('collage-wrapper');
	        var newImage = document.createElement("img");
	        
	        newImage.setAttribute('src', url);
	        newImage.style.left = (4*index) + "vw";
	        newImage.style.zIndex = index;
	        newImage.style.top = "7vh";
	        newImage.style.position = "absolute";
	        newImage.style.minWidth = "25%";
	        newImage.style.minHeight = "55%";
	        newImage.style.maxWidth = "35%";
	        newImage.style.maxHeight = "70%";
	        
	    	//Apply random rotation between -45 and 45
	    	var degree = Math.floor(Math.random() * 91) - 45;
	    	var rotate = 'rotate(' + degree + 'deg)';
	    	newImage.style.WebkitTransform = rotate;
	    	newImage.style.MozTransform = rotate;
	    	newImage.style.OTransform = rotate;
	    	newImage.style.msTransform = rotate;
	    	newImage.style.transform = rotate;
	    	
	        console.log(destination.appendChild(newImage));
	    }
	
		function redirectToRecipe(link){
			window.location = link;
		}
		
		function redirectToRestaurant(link){
			window.location = link;
		}
		
		function redirectToResult(link) {
			var term ="<%= term %>";
			var trimmed = term.replace(" ", "_");
			window.location = link+"&term=" +trimmed;
		}
		function redirectManageList(index){
			var list = "";
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			if(list != ""){
				redirectToResult("/ImHungry/RedirectionController?action=managelist" +"&index=" + index + "&list=" + list);
			}
		}
	
	</script>
	<div id="outer-wrapper" class="container" style="height: 100vh">
        <div class="row my-5">
            <div class="col h-100">
                <div id="collage-wrapper">
                </div>

                <div id="title-wrapper" class="container my-5">
                	<% String trimmed = term.replaceAll("\\_", " "); %>
                    <h1 class="mx-auto display-4">Results for <%=trimmed%></h1>
                </div>

                <div id="results-wrapper" class="container">
                    <div class="row">
                        <div class="col mx-4">
                            <table class="table-dark table-hover table-striped table-borderless">
                                <thead>
                                    <tr>
                                        <h3>Restaurants</h3>
                                    </tr>
                                </thead>
                                <tbody>	                                
                                	<%
                                	System.out.println("page number=" + pagenumber);
		                    		int rest_upperbound = 0;
                    				System.out.println("number of restaurants" + rm.getNumberOfRestaurants());
		                            if ((5 * (pagenumber - 1) + 5) < rm.getNumberOfRestaurants()) {
		                            	rest_upperbound = (5 * (pagenumber - 1) + 5);
		                            } else {
		                            	rest_upperbound = rm.getNumberOfRestaurants();
		                            }
                                	for (int i = 5 * (pagenumber - 1) ; i < rest_upperbound ; i++) {
                                		System.out.println("i=" + i);
									//for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++){
										Map<String, String> resultsFields = rm.getFormattedRestaurantResultsAt(i);
										
										// Skip do not show results
										if(resultsFields.get("modifier").equals("donotshow")){
											continue;
										}
									
									%>
                                
                                    <tr onclick=<%="redirectToRestaurant(\"" + "/ImHungry/RedirectionController?action=restaurant&term="+term +"&index=" + index + "&item=" + i + "\")"%>>
                                        <td class="col">
                                            <table class="table text-white">
                                                <tbody>
                                                    <tr style="background-color: inherit;">
                                                        <td><%=resultsFields.get("name")%></td>
                                                        <td><%=resultsFields.get("stars")%></td>
                                                    </tr>
                                                    <tr style="background-color: inherit;">
                                                        <td><%=resultsFields.get("drivingTime")%></td>
                                                        <td><%=resultsFields.get("address")%></td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                        <td class="col"><%=resultsFields.get("priceRange")%></td>
                                    </tr>
                                    
                                    <%
                                    }
                                    %>
                                </tbody>
                            </table>
                        </div>
                        <div class="col mx-4">
                            <table class="table-dark table-hover table-striped table-borderless">
                                <thead>
                                    <tr>
                                        <h3>Recipes</h3>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                String ecodedValue = URLEncoder.encode(term, "UTF-8");
                                int reci_upperbound = 0;
                                if ((5 * (pagenumber - 1) + 5) < rm.getNumberOfRecipes()) {
                                	reci_upperbound = (5 * (pagenumber - 1) + 5);
                                } else {
                                	reci_upperbound = rm.getNumberOfRecipes();
                                }
                                for (int i = 5 * (pagenumber - 1) ; i < reci_upperbound ; i++) {
									Map<String, String> resultsFields = rm.getFormattedRecipeResultsAt(i);
									
									// Skip do not show results
									if(resultsFields.get("modifier").equals("donotshow")){
										continue;
									}
								
								%>
                                    <tr onclick=<%="redirectToRecipe(\"" + "/ImHungry/RedirectionController?action=recipe&term="+ecodedValue +"&index=" + index + "&item=" + i + "\")"%>>
                              
                                        <td class="col">
                                            <table class="table text-white">
                                                <tbody>
                                                    <tr style="background-color: inherit;">
                                                        <td><%=resultsFields.get("name")%></td>
                                                        <td><%=resultsFields.get("stars")%></td>
                                                    </tr>
                                                    <tr style="background-color: inherit;">
                                                        <td>Prep: <%=resultsFields.get("prepTime") + " minutes"%></td>
                                                        <td>Cook: <%=resultsFields.get("cookTime") + " minutes"%></td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    
                                 <%
								}
                                 %>
                                </tbody>
                            </table>
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
                           
                            <li class="nav-item mb-3">
		                       <input class="btn btn-secondary" onclick=<%="redirectManageList("+ index +")"%> type="button" value="Manage Lists"/>
		                   </li>
                            <li class="nav-item">
                                <a class="btn btn-secondary" onclick=<%="redirectToRecipe(\"" + "/ImHungry/ResultsPageController?action=search&term="+ term +"&index=" + index +  "&token=" + token + "\")"%>>Return to Search</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-secondary" onclick=<%="redirectToRecipe(\"" + "/ImHungry/SignInView.jsp\")"%>>Sign Out</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!-- TODO pagination -->
    	<div class="container my-6 mr-5 pr-5 pb-5">

 			<nav aria-label="...">
				<ul class="pagination pagination-lg">
				<% 
					int maxPages = Math.max(rm.getNumberOfRecipes() / 5, rm.getNumberOfRestaurants() / 5);
					for (int k = 1 ; k <= maxPages ; k++){
						if (pagenumber == k) {
				%>
						<li class="page-item active" aria-current="page">
							<span class="page-link"><%=k%><span class="sr-only">(current)</span></span>
						</li>
				<%
						} else {
				%>
						<li class="page-item"><a id=<%="page-alt"+k%> class="page-link" href=<%="/ImHungry/SearchPageController?action=search&term=" + term + "&token=" + token + "&limit=" + lim + "&radius=" + rad + "&page=" + k %>><%= k %></a></li>
				<%
						}
				   }
				%>					
				</ul>
			</nav>
		</div>
    </div>
</body>
</html>