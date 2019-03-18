<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="models.ResponseModel"%>   
<%@page import="java.util.Map"%> 

<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html>
<html>
<head>
	<title>AAAAAaAAaAAaAAaaaaaAaaaAa</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
	<style>
	    body {
	        background-color: #f5f5f5; /* white smoke background */
	    }
	    #wrapper {
	        margin-top: 20px !important;
	    }
	    .title {
	        margin-top: 1em;
	    }
	    #itemsTable {
	        width: 200px;
	    }
	    .buttons {
	        float: right;
	    }
	</style>
</head>
<body>
   <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
   <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<% String term = (String) request.getAttribute("term");
	String ecodedValue = URLEncoder.encode(term, "UTF-8");
	%>
	<script>
	
		var list = "";
	
		function redirectToRecipe(link){
			window.location = link;
		}
		
		function redirectToRestaurant(link){
			window.location = link;
		}
		
		function redirectToManageList(index){
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			if(list != ""){
				var term ="<%= term %>";
				var trimmed = term.replace(" ", "_");
				window.location.replace("/ImHungry/RedirectionController?action=managelist&term="+trimmed + "&index=" + index + "&list=" + list);
			}
		}
		
		function moveToList(index, item, type){
			console.log("blah");
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			if(list != ""){
				var xhr = new XMLHttpRequest();
				var term ="<%= term %>";
				var trimmed = term.replace(" ", "_");
				xhr.open("GET", "/ImHungry/RedirectionController?action=movetolist&term="+trimmed+ "&index=" + index + "&list=" + list + "&item=" + item + "&type=" + type, true);
				xhr.send();
			}
		}
		
		function removeFromList(index, item, originalList, type){
			var e = document.getElementById("managelistselect");
			var xhr = new XMLHttpRequest();
			var term ="<%= term %>";
			var trimmed = term.replace(" ", "_");
			xhr.open("GET", "/ImHungry/RedirectionController?action=removefromlist&term="+trimmed + "&index=" + index + "&list=" + originalList + "&item=" + item + "&type=" + type, true);
			xhr.send();
		}
		
	</script>

	<%
		ResponseModel rm = ((ResponseModel)request.getAttribute("response"));
		String title = (String)request.getAttribute("title");
		int index = (int)request.getAttribute("index");
	%>

   <div id="wrapper" class="container">
       <div class="row">
           <div class="col-md-8">
                   <div class="col-md-12 m-5">
                       <div class="container">
                           <h1 class="display-4"><%=title%></h1>
                       </div>
                   </div>
               <div class="row">
                   <div class="col-lg-10">
                       <div class="container">
                           <table class="table table-striped table-borderless">
                               <thead>
                               </thead>
                               <tbody>
                                   <%
									int count = 0;
									for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++){
										count += 1;
										Map<String, String> resultsFields = rm.getFormattedRecipeResultsAt(i);
										
										// Skip do not show results
										if(title.equals("Do Not Show") && !resultsFields.get("modifier").equals("donotshow")){
											continue;
										}
										if(title.equals("Favorites") && !resultsFields.get("modifier").equals("favorites")){
											continue;
										}
										if(title.equals("To Explore") && !resultsFields.get("modifier").equals("toexplore")){
											continue;
										}
									%>
										<tr>
			                              <td>
			                                  <div class="container h-100">
			                                      <div class="h-100 row justify-content-center align-items-center">
			                                          <table>
			                                              <tbody>
			                                                  <tr style="background-color:inherit" onclick=<%="redirectToRecipe(\"" + "/ImHungry/RedirectionController?action=recipe&term="+ecodedValue +"&index=" + index + "&item=" + i + "\")"%>>
							                                    <td>
								                                    <table class="table">
						                                                <tbody>
						                                                    <tr style="background-color: inherit;">
						                                                        <td>name: <%=resultsFields.get("name")%></td>
						                                                        <td>stars: <%=resultsFields.get("stars")%></td>
						                                                    </tr>
						                                                    <tr style="background-color: inherit;">
						                                                        <td>prep: <%=resultsFields.get("prepTime")%></td>
						                                                        <td>cook: <%=resultsFields.get("cookTime")%></td>
						                                                    </tr>
						                                                </tbody>
						                                            </table>
							                                    </td>
							                                </tr>
							                            </tbody>
							                        </table>
								                  </div>
								              </div>
								            </td>
								            <td>
								                <div class="container">
								                    <div class="buttons">
								                        <table>
								                            <tbody>
								                                <tr style="background-color:inherit">
								                                    <td>
								                                        <button type="button" class="btn btn-default btn-sm"
								                                        	onclick=<%= "removeFromList(" + index + "," + i + "," + "\"" + resultsFields.get("modifier") + "\"" + ",\"recipe\")"%>>
								                                            <i class="fas fa-times"></i>
								                                        </button>
								                                    </td>
								                                </tr>
								                                <tr style="background-color:inherit">
								                                    <td>
								                                        <button type="button" class="btn btn-default btn-sm"
								                                        	onclick=<%= "moveToList(" + index + "," + i + ",\"recipe\")"%> >
								                                            <i class="fas fa-sign-out-alt"></i>
								                                        </button>
								                                    </td>
								                                </tr>
								                            </tbody>
								                        </table>
								                    </div>
								                </div>
								            </td>
								        </tr>
									<%
									}
									%>
									
									<%
									
									for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++){
										count += 1; 
										Map<String, String> resultsFields = rm.getFormattedRestaurantResultsAt(i);
					
										// Skip do not show results
										if(title.equals("Do Not Show") && !resultsFields.get("modifier").equals("donotshow")){
											continue;
										}
										if(title.equals("Favorites") && !resultsFields.get("modifier").equals("favorites")){
											continue;
										}
										if(title.equals("To Explore") && !resultsFields.get("modifier").equals("toexplore")){
											continue;
										}
									 %>
										 <tr>
	                                       <td>
	                                           <div class="container h-100">
	                                               <div class="h-100 row justify-content-center align-items-center">
	                                                   <table>
	                                                       <tbody>
	                                                           <tr style="background-color:inherit" onclick=<%="redirectToRestaurant(\"" + "/ImHungry/RedirectionController?action=restaurant&term="+term +"&index=" + index + "&item=" + i + "\")"%>>
	                                                               <td class="col">
							                                            <table class="table">
							                                                <tbody>
							                                                    <tr style="background-color: inherit;">
							                                                        <td>name: <%=resultsFields.get("name")%></td>
							                                                        <td>stars: <%=resultsFields.get("stars")%></td>
							                                                    </tr>
							                                                    <tr style="background-color: inherit;">
							                                                        <td>minutes: <%=resultsFields.get("drivingTime")%></td>
							                                                        <td>address: <%=resultsFields.get("address")%></td>
							                                                    </tr>
							                                                </tbody>
							                                            </table>
							                                        </td>
							                                        <td class="col">
							                                        	<%=resultsFields.get("priceRange")%>
							                                        </td>
	                                                           </tr>
	                                                       </tbody>
	                                                   </table>
	                                               </div>
	                                           </div>
	                                       </td>
	                                       <td>
	                                           <div class="container">
	                                               <div class="buttons">
	                                                   <table>
	                                                       <tbody>
	                                                           <tr style="background-color:inherit">
	                                                               <td>
	                                                                   <button type="button" class="btn btn-default btn-sm"
	                                                                  		onclick=<%= "removeFromList("+ index + "," + i + "," + "\"" + resultsFields.get("modifier") + "\"" + ",\"restaurant\")"%>>
	                                                                       <i class="fas fa-times"></i>
	                                                                   </button>
	                                                               </td>
	                                                           </tr>
	                                                           <tr style="background-color:inherit">
	                                                               <td>
	                                                                   <button type="button" class="btn btn-default btn-sm"
	                                                                  		onclick=<%= "moveToList(" + index + "," + i + ",\"restaurant\")"%>>
	                                                                       <i class="fas fa-sign-out-alt"></i>
	                                                                   </button>
	                                                               </td>
	                                                           </tr>
	                                                       </tbody>
	                                                   </table>
	                                               </div>
	                                           </div>
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
                       <input class="btn btn-secondary" onclick=<%="redirectToManageList("+ index + ")"%> type="button" value="Manage Lists">
                   </li>
                   <li class="nav-item">
                       <a class="btn btn-secondary" onclick=<%= "redirectToRecipe(\"" + "/ImHungry/ResultsPageController?action=search&term="+ term + "&index=" + index + "\")" %>>Back to Search</a>
                   </li>
                   <li class="nav-item my-3">
                       <a class="btn btn-secondary" onclick=<%= "redirectToRecipe(\"" + "/ImHungry/RedirectionController?action=results&term="+ term +"&index=" + index + "\")" %>>Back to Results</a>
                   </li>
               </ul>
           </div>
       	</div>
      </div>
	
</body>
</html>