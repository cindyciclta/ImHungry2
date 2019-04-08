<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="models.ResponseModel"%>   
<%@page import="models.CollageGenerationModel"%>   
<%@page import="models.GoogleImageRequestModel"%> 
<%@page import="java.util.Map"%>  
<%@page import="java.util.ArrayList"%> 
<%@page import="org.json.JSONArray"%> 
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Vector"%>
<%@page import="models.SearchTermModel"%>
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
		.miniCollage {
			margin-right: 3vw;
			margin-left: 3vw;
			margin-bottom: 3vh;
			width: 8.5vw;
			height: 13vh;
		}
		
		#hist {
			margin-bottom: 2vh;
		}
		
		#table-outer-scroll {
			overflow-x: scroll;
		}
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<title>Results Page - I'm Hungry</title>
</head>
<body>
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
	String rest_pg = (String) request.getAttribute("rest_page");
	String reci_pg = (String) request.getAttribute("reci_page");
	int lim = Integer.parseInt((String)request.getSession().getAttribute("limit"));
	int rad = Integer.parseInt((String)request.getSession().getAttribute("radius"));
	int reci_pgnum;
	int rest_pgnum;
	try {
		rest_pgnum = Integer.parseInt(rest_pg);
		reci_pgnum = Integer.parseInt(reci_pg);
	} catch (Exception e) {
		rest_pgnum = 1;
		reci_pgnum = 1;
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
	        createMiniCollages();
	    });
		
		function createMiniCollages() {
	        var miniCollages = document.getElementsByClassName("miniCollage");
	        for (var x = 0; x < miniCollages.length; x++) {
	        	var currCollage = miniCollages[x];
	        	var currCollageImages = currCollage.getElementsByClassName("miniCollageImg");
	        	for (var y = 0; y < currCollageImages.length; y++) {
	        		var currImg = currCollageImages[y];
	        		currImg.style.zIndex = y;
	        		currImg.style.maxWidth = "28%";
	        		currImg.style.maxHeight = "28%";
	        		
	    	    	//Apply random rotation between -45 and 45
	    	    	var degree = Math.floor(Math.random() * 91) - 45;
	    	    	var rotate = 'rotate(' + degree + 'deg)';
	    	    	currImg.style.WebkitTransform = rotate;
	    	    	currImg.style.MozTransform = rotate;
	    	    	currImg.style.OTransform = rotate;
	    	    	currImg.style.msTransform = rotate;
	    	    	currImg.style.transform = rotate;
	        	}
	        }
		}
	
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
	    	
	        destination.appendChild(newImage)
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
                                <!-- Restaurant results -->
                                <tbody>	                                
                                	<%
                    				System.out.println("restaurant page number=" + rest_pgnum);
		                    		int rest_upperbound = 0;
                    				System.out.println("number of restaurants" + rm.getNumberOfRestaurants());
		                            if ((5 * (rest_pgnum - 1) + 5) < rm.getNumberOfRestaurants()) {
		                            	rest_upperbound = (5 * (rest_pgnum - 1) + 5);
		                            } else {
		                            	rest_upperbound = rm.getNumberOfRestaurants();
		                            }
                                    if (rm.getNumberOfRestaurants() == 0) {
                                        %>
                                        <h3>No Results</h3>
                                        <%
                                    }
                                	for (int i = 5 * (rest_pgnum - 1) ; i < rest_upperbound ; i++) {
                                		System.out.println("restaurant #" + i);
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
                            <!-- Restaurant pagination -->
					    	<div class="container my-6 mr-5 pr-5 pb-5">
					 			<nav aria-label="Restaurant pagination">
									<ul class="pagination pagination-lg justify-content-center">
									<% 
										int restMaxPages = Math.max(rm.getNumberOfRecipes() / 5, rm.getNumberOfRestaurants() / 5);
										for (int k = 1 ; k <= restMaxPages ; k++){
											if (rest_pgnum == k) {
									%>
											<li class="page-item active" aria-current="page">
												<span class="page-link"><%=k%><span class="sr-only">(current)</span></span>
											</li>
									<%
											} else {
									%>
											<li class="page-item"><a id=<%="rest-page-alt"+k%> class="page-link" href=<%="/ImHungry/SearchPageController?action=search&term=" + term + "&token=" + token + "&limit=" + lim + "&radius=" + rad + "&rest_page=" + k + "&reci_page=" + reci_pgnum %>><%= k %></a></li>
									<%
											}
									   }
									%>					
									</ul>
								</nav>
							</div>
                        </div>
                        <div class="col mx-4">
                            <table class="table-dark table-hover table-striped table-borderless">
                                <thead>
                                    <tr>
                                        <h3>Recipes</h3>
                                    </tr>
                                </thead>
                                <!-- Recipe results  -->
                                <tbody>
                                <%
                                String ecodedValue = URLEncoder.encode(term, "UTF-8");
                                int reci_upperbound = 0;
                            	System.out.println("recipe page number=" + reci_pgnum);
                                if ((5 * (reci_pgnum - 1) + 5) < rm.getNumberOfRecipes()) {
                                	reci_upperbound = (5 * (reci_pgnum - 1) + 5);
                                } else {
                                	reci_upperbound = rm.getNumberOfRecipes();
                                }
                                for (int i = 5 * (reci_pgnum - 1) ; i < reci_upperbound ; i++) {
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
                            <!-- Recipe pagination -->
					    	<div class="container my-6 mr-5 pr-5 pb-5">
					 			<nav aria-label="Recipe pagination">
									<ul class="pagination pagination-lg justify-content-center">
									<% 
										int reciMaxPages = Math.max(rm.getNumberOfRecipes() / 5, rm.getNumberOfRestaurants() / 5);
										for (int k = 1 ; k <= reciMaxPages ; k++){
											if (reci_pgnum == k) {
									%>
											<li class="page-item active" aria-current="page">
												<span class="page-link"><%=k%><span class="sr-only">(current)</span></span>
											</li>
									<%
											} else {
									%>
											<li class="page-item"><a id=<%="rest-page-alt"+k%> class="page-link" href=<%="/ImHungry/SearchPageController?action=search&term=" + term + "&token=" + token + "&limit=" + lim + "&radius=" + rad + "&rest_page=" + rest_pgnum + "&reci_page=" + k %>><%= k %></a></li>
									<%
											}
									   }
									%>					
									</ul>
								</nav>
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
		                       <input class="btn btn-secondary" onclick=<%="redirectManageList("+ index +")"%> type="button" value="Manage Lists"/>
		                   </li>
                            <li class="nav-item mb-3">
                                <a class="btn btn-secondary" onclick=<%="redirectToRecipe(\"" + "/ImHungry/ResultsPageController?action=search&term="+ term +"&index=" + index +  "&token=" + token + "\")"%>>Return to Search</a>
                            </li>
                            <li class="nav-item">
                                <a id="signOutLink" class="btn btn-secondary" onclick=<%="redirectToRecipe(\"" + "/ImHungry/SignInView.jsp\")"%>>Sign Out</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

		<div class="row justify-content-center align-items-center">
			<p7 id="hist">Search History</p7>
		</div>
		<div id="table-outer-scroll">
			<table class="table-dark table-hover table-stripped">
				<thead></thead>
				<tbody>
					<%
					Vector<SearchTermModel> searches = (Vector<SearchTermModel>)request.getAttribute("searches");
					%>
					<tr>
						<%
						for (SearchTermModel search : searches) {
						%>
						<td>
							<div class="miniCollage">
								<%
								for (String im : search.images) {
								%>
								<img class="miniCollageImg" src=<%=im%>>
								<%
								}
								%>
							</div>
						</td>
						<%
						}
						%>
					</tr>
					<tr>
						<%
						int i=1;
						for (SearchTermModel search : searches) {
						%>
						<td class="text-center">
							<p1 id=<%="searchHist" + i%>><a href=<%="\"/ImHungry/SearchPageController?action=search&term=" + search.term + "&token=" + token + "&limit=" + search.limit + "&radius=" + search.radius + "&page=1\""%>><%=search.term%></a></p1>
						</td>
						<%
						i++;
						}
						%>
					</tr>
				</tbody>
			</table>
		</div>
		

    </div>
    
	<!--Mouse glitter stuff-->
	<script type="text/javascript" src="js/jquery.1.11.1.js"></script>
	<script>
		var colour="random"; 
		var sparkles=100; // Number of sparkles falling
		var x=ox=400;
		var y=oy=300;
		var swide=800;
		var shigh=600;
		var sleft=sdown=0;
		var tiny=new Array();
		var star=new Array();
		var starv=new Array();
		var starx=new Array();
		var stary=new Array();
		var tinyx=new Array();
		var tinyy=new Array();
		var tinyv=new Array();
	
		colours=new Array('#ff0000','#00ff00','#ffffff','#ff00ff','#ffa500','#ffff00','#00ff00','#ffffff','ff00ff')
	
		n = 10;
		y = 0;
		x = 0;
		n6=(document.getElementById&&!document.all);
		ns=(document.layers);
		ie=(document.all);
		d=(ns||ie)?'document.':'document.getElementById("';
		a=(ns||n6)?'':'all.';
		n6r=(n6)?'")':'';
		s=(ns)?'':'.style';
	
		if (ns){
			for (i = 0; i < n; i++)
				document.write('<layer name="dots'+i+'" top=0 left=0 width='+i/2+' height='+i/2+' bgcolor=#ff0000></layer>');
		}
	
		if (ie)
			document.write('<div id="con" style="position:absolute;top:0px;left:0px"><div style="position:relative">');
	
		if (ie||n6){
			for (i = 0; i < n; i++)
				document.write('<div id="dots'+i+'" style="position:absolute;top:0px;left:0px;width:'+i/2+'px;height:'+i/2+'px;background:#ff0000;font-size:'+i/2+'"></div>');
		}
	
		if (ie)
			document.write('</div></div>');
		(ns||n6)?window.captureEvents(Event.MOUSEMOVE):0;
	
		function Mouse(evnt){
	
			y = (ns||n6)?evnt.pageY+4 - window.pageYOffset:event.y+4;
			x = (ns||n6)?evnt.pageX+1:event.x+1;
		}
	
		//(ns)?window.onMouseMove=Mouse:document.onmousemove=Mouse;
	
		function animate(){
	
			o=(ns||n6)?window.pageYOffset:0;
	
			if (ie)con.style.top=document.body.scrollTop + 'px';
	
			for (i = 0; i < n; i++){
	
				var temp1 = eval(d+a+"dots"+i+n6r+s);
	
				randcolours = colours[Math.floor(Math.random()*colours.length)];
	
				(ns)?temp1.bgColor = randcolours:temp1.background = randcolours; 
	
				if (i < n-1){
	
					var temp2 = eval(d+a+"dots"+(i+1)+n6r+s);
					temp1.top = parseInt(temp2.top) + 'px';
					temp1.left = parseInt(temp2.left) + 'px';
	
				} 
				else{
	
					temp1.top = y+o + 'px';
					temp1.left = x + 'px';
				}
			}
	
			setTimeout("animate()",10);
		}
	
		//animate();
	
		window.onload=function() { if (document.getElementById) {
			var i, rats, rlef, rdow;
			for (var i=0; i<sparkles; i++) {
				var rats=createDiv(3, 3);
				rats.style.visibility="hidden";
				rats.style.zIndex="999";
				document.body.appendChild(tiny[i]=rats);
				starv[i]=0;
				tinyv[i]=0;
				var rats=createDiv(5, 5);
				rats.style.backgroundColor="transparent";
				rats.style.visibility="hidden";
				rats.style.zIndex="999";
				var rlef=createDiv(1, 5);
				var rdow=createDiv(5, 1);
				rats.appendChild(rlef);
				rats.appendChild(rdow);
				rlef.style.top="2px";
				rlef.style.left="0px";
				rdow.style.top="0px";
				rdow.style.left="2px";
				document.body.appendChild(star[i]=rats);
			}
			set_width();
			sparkle();
		}}
	
		function sparkle() {
			var c;
			if (Math.abs(x-ox)>1 || Math.abs(y-oy)>1) {
				ox=x;
				oy=y;
				for (c=0; c<sparkles; c++) if (!starv[c]) {
					star[c].style.left=(starx[c]=x)+"px";
					star[c].style.top=(stary[c]=y+1)+"px";
					star[c].style.clip="rect(0px, 5px, 5px, 0px)";
					star[c].childNodes[0].style.backgroundColor=star[c].childNodes[1].style.backgroundColor=(colour=="random")?newColour():colour;
					star[c].style.visibility="visible";
					starv[c]=50;
					break;
				}
			}
			for (c=0; c<sparkles; c++) {
				if (starv[c]) update_star(c);
				if (tinyv[c]) update_tiny(c);
			}
			setTimeout("sparkle()", 40);
		}
	
		function update_star(i) {
			if (--starv[i]==25) star[i].style.clip="rect(1px, 4px, 4px, 1px)";
			if (starv[i]) {
				stary[i]+=1+Math.random()*3;
				starx[i]+=(i%5-2)/5;
				if (stary[i]<shigh+sdown) {
					star[i].style.top=stary[i]+"px";
					star[i].style.left=starx[i]+"px";
				}
				else {
					star[i].style.visibility="hidden";
					starv[i]=0;
					return;
				}
			}
			else {
				tinyv[i]=50;
				tiny[i].style.top=(tinyy[i]=stary[i])+"px";
				tiny[i].style.left=(tinyx[i]=starx[i])+"px";
				tiny[i].style.width="2px";
				tiny[i].style.height="2px";
				tiny[i].style.backgroundColor=star[i].childNodes[0].style.backgroundColor;
				star[i].style.visibility="hidden";
				tiny[i].style.visibility="visible"
			}
		}
	
		function update_tiny(i) {
			if (--tinyv[i]==25) {
				tiny[i].style.width="1px";
				tiny[i].style.height="1px";
			}
			if (tinyv[i]) {
				tinyy[i]+=1+Math.random()*3;
				tinyx[i]+=(i%5-2)/5;
				if (tinyy[i]<shigh+sdown) {
					tiny[i].style.top=tinyy[i]+"px";
					tiny[i].style.left=tinyx[i]+"px";
				}
				else {
					tiny[i].style.visibility="hidden";
					tinyv[i]=0;
					return;
				}
			}
			else tiny[i].style.visibility="hidden";
		}
	
		//document.onmousemove=mouse;
		function mouse(e) {
			if (e) {
				y=e.pageY;
				x=e.pageX;
			}
			else {
				set_scroll();
				y=event.y+sdown;
				x=event.x+sleft;
			}
		}
	
		window.onscroll=set_scroll;
		function set_scroll() {
			if (typeof(self.pageYOffset)=='number') {
				sdown=self.pageYOffset;
				sleft=self.pageXOffset;
			}
			else if (document.body && (document.body.scrollTop || document.body.scrollLeft)) {
				sdown=document.body.scrollTop;
				sleft=document.body.scrollLeft;
			}
			else if (document.documentElement && (document.documentElement.scrollTop || document.documentElement.scrollLeft)) {
				sleft=document.documentElement.scrollLeft;
				sdown=document.documentElement.scrollTop;
			}
			else {
				sdown=0;
				sleft=0;
			}
		}
	
		window.onresize=set_width;
		function set_width() {
			var sw_min=999999;
			var sh_min=999999;
			if (document.documentElement && document.documentElement.clientWidth) {
				if (document.documentElement.clientWidth>0) sw_min=document.documentElement.clientWidth;
				if (document.documentElement.clientHeight>0) sh_min=document.documentElement.clientHeight;
			}
			if (typeof(self.innerWidth)=='number' && self.innerWidth) {
				if (self.innerWidth>0 && self.innerWidth<sw_min) sw_min=self.innerWidth;
				if (self.innerHeight>0 && self.innerHeight<sh_min) sh_min=self.innerHeight;
			}
			if (document.body.clientWidth) {
				if (document.body.clientWidth>0 && document.body.clientWidth<sw_min) sw_min=document.body.clientWidth;
				if (document.body.clientHeight>0 && document.body.clientHeight<sh_min) sh_min=document.body.clientHeight;
			}
			if (sw_min==999999 || sh_min==999999) {
				sw_min=800;
				sh_min=600;
			}
			swide=sw_min;
			shigh=sh_min;
		}
	
		function createDiv(height, width) {
			var div=document.createElement("div");
			div.style.position="absolute";
			div.style.height=height+"px";
			div.style.width=width+"px";
			div.style.overflow="hidden";
			return (div);
		}
	
		function newColour() {
			var c=new Array();
			c[0]=255;
			c[1]=Math.floor(Math.random()*256);
			c[2]=Math.floor(Math.random()*(256-c[1]/2));
			c.sort(function(){return (0.5 - Math.random());});
			return ("rgb("+c[0]+", "+c[1]+", "+c[2]+")");
		}
	</script>
</body>
</html>