<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
	<!-- Eclipse put this here.. -->
    <!-- <meta charset="ISO-8859-1"> -->

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <title>Insert title here</title>

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
        }

        .overlays {
            position: absolute;
        } 

        .nested-tr {
            background-color: inherit;
        }
		
		body, html {
			height: 100%;
			font-family: "Comic Sans MS", cursive, sans-serif;
		}
		
        /*.navbar.fixed-right {
            position: fixed;
            top: 0;
            right: 0;
            z-index: 0;
        }*/
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<meta charset="ISO-8859-1">
	<title>Results Page</title>
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
		        	showImage(refactorstring);
	        	}
			}

	        $('img').each(function() {
	            var deg = randomIntFromInterval(-45, 45);
	            console.log('rotated', deg)
	            var numberOfImages = $('img').length;
	            var rotate = 'rotate(' + deg + 'deg)';
	            //console.log($(this).height());
	            var width = (100-5)/((numberOfImages <= 10) ? numberOfImages : 10)*2;
	            $(this).css({ 
	                '-webkit-transform': rotate,
	                '-moz-transform': rotate,
	                '-o-transform': rotate,
	                '-ms-transform': rotate,
	                'transform': rotate,
	                //'width': width+'%'
	                'width': 15+'vw'
	            });
	        });
	    });
	
	    // min = minimum rotation in degrees, max = maximum rotation in degrees
	    function randomIntFromInterval(min,max) {
	        return Math.floor(Math.random()*(max-min+1)+min);
	    }
	
	    function showImage(url) {
	        // myImage : ID of image on which to place new image
	
	        var destination = document.getElementById('collage-wrapper');
	        
	        console.log('here', $('#collage-wrapper').width());
	        
	        margin = 20;
	        
	        l = destination.offsetLeft + $('#collage-wrapper').width()/4;
	        t = destination.offsetTop + $('#collage-wrapper').height()/4;
	        //w = destination.width;
	        //h = destination.height;
	        w = $('#collage-wrapper').width()/2
	        h = $('#collage-wrapper').height()/2
	        
	        // Location inside the image
	        offX = parseInt(Math.random() * w);
	        offY = parseInt(Math.random() * h);
	        
	        if(offX > margin) offX -= margin;
	        if(offY > margin) offY -= margin;
	
	        l += offX;
	        t += offY;
	
	        var newImage = document.createElement("img");
	        //newImage.setAttribute('src', 'https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg');
	        
	        newImage.setAttribute('src', url);
	        newImage.setAttribute('class', 'overlays');
	        newImage.style.left = l + "px";
	        newImage.style.top = t + "px";
	        console.log(document.body.appendChild(newImage));
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
				//window.location.replace("/ImHungry/RedirectionController?action=managelist" +"&index=" + index + "&list=" + list+ "&=term" + term);
				redirectToResult("/ImHungry/RedirectionController?action=managelist" +"&index=" + index + "&list=" + list);
			}
		}
	
	</script>
	<div id="outer-wrapper" class="container" style="height: 100vh">
        <div class="row my-5">
            <div class="col h-100">
                <div id="collage-wrapper" class="container my-6 mr-5 pr-5 pb-5">
                    <!-- TODO Collage here -->

                    <!-- <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid">
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--fX47AfFz--/t_Preview/b_rgb:0f7b47,c_limit,f_jpg,h_630,q_90,w_630/v1465397214/production/designs/536781_1.jpg" class="img-fluid"> -->

                </div>

                <div id="title-wrapper" class="container my-5">
                	<% String trimmed = term.replaceAll("\\_", " "); %>
                    <h1 class="mx-auto display-4">Results for <%=trimmed%></h1>
                </div>

                <div id="results-wrapper" class="container">
                    <!-- TODO table here -->
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
									for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++){
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
								for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++){
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
		                       <input class="btn btn-secondary" onclick=<%="redirectManageList("+ index +")"%> type="button" value="Manage Lists">
		                   </li>
                            <li class="nav-item">
                            	
                                <a class="btn btn-secondary" onclick=<%="redirectToRecipe(\"" + "/ImHungry/ResultsPageController?action=search&term="+ term +"&index=" + index + "\")"%>>Return to Search</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>