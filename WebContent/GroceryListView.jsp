<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="models.ResponseModel"%> 
<%@page import="models.GroceryListModel"%>  
<%@page import="java.util.Map"%> 

<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html>
<html>
<head>
	<title>Grocery List - I'm Hungry</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
	<!-- jsDelivr :: Sortable :: Latest (https://www.jsdelivr.com/package/npm/sortablejs) -->
	<script src="https://cdn.jsdelivr.net/npm/sortablejs@latest/Sortable.min.js"></script>
	<style>
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
		/* for making every other table row higher contrast than default */
		.table-dark.table-striped tbody tr:nth-of-type(odd) {
		    background-color: rgba(255,255,255,.2);
		}
	</style>
</head>
<body>
   <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
   <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	<% String term = (String) request.getAttribute("term");
	String token = (String)request.getAttribute("token");
	String ecodedValue = URLEncoder.encode(term, "UTF-8");
	%>
	<script>
	
		function redirectToRecipe(link){
			window.location = link;
		}
		
		function redirectToManageList(index){
			var term ="<%= term %>";
			var trimmed = term.replace(" ", "_");
			var list = "";
			var e = document.getElementById("managelistselect");
			list = e.options[e.selectedIndex].value;
			
			
			if(list !== ""){
				var token = <%="\"" + token + "\""%>;
				if(list === "grocery"){
					var link = "/ImHungry/RedirectionController?action=managegrocerylist" +"&index=" + index + "&token=" + token + "&term=" + trimmed + "&token=" + token;
					window.location = link;
				}else{
					var link = "/ImHungry/RedirectionController?action=managelist" +"&index=" + index + "&list=" + list + "&term=" + trimmed + "&token=" + token;
					window.location = link;
				}
			}
		}
		
		function removeFromList(index, item){
			var xhr = new XMLHttpRequest();
			var term ="<%= term %>";
			var token = <%= "\"" + token + "\""%>;
			var trimmed = term.replace(" ", "_");
			xhr.open("GET", "/ImHungry/RedirectionController?action=deletegrocery&term="+item + "&index=" + index + "&token=" + token + "&item=" + item , true);
			xhr.send();
		}
		
	</script>

	<%
		ResponseModel rm = ((ResponseModel)request.getAttribute("response"));
		GroceryListModel g = ((GroceryListModel)request.getAttribute("groceries"));
		
		String index = (String)request.getAttribute("index");
	%>

   <div id="wrapper" class="container">
       <div class="row">
           <div class="col-md-8">
                   <div class="col-md-12 m-5">
                       <div class="container">
                           <h1 class="display-4"><%="Grocery List"%></h1>
                       </div>
                   </div>
               <div class="row">
                   <div class="col-lg-10">
                       <div class="container">
                           <table class="table-dark table-hover table-striped table-borderless">
                               <thead>
                               </thead>
                               <tbody id="movable-list">
                                   <%
                                   
									for(int i = 0 ; i < g.getSize() ; i++){
									
									%>
											<tr>
											  <td>
								                <div class="container">
								                    <div>
								                        <table>
								                            <tbody>
								                                <tr style="background-color:inherit">
								                                    <td>
								                                      <i class="fas fa-arrows-alt movable-list-handle"></i>
								                                    </td>
								                                </tr>
								                            </tbody>
								                        </table>
								                    </div>
								                </div>
								              </td>
				                              <td>
				                                  <div class="container h-100">
				                                      <div class="h-100 row justify-content-center align-items-center">
				                                          <table>
				                                              <tbody>
				                                                 <tr style="background-color:inherit">
								                                    <td>
									                                    <table class="table text-white">
							                                                <tbody>
							                                                    <tr style="background-color: inherit;">
							                                                        <td><%=g.getItem(i)%></td>
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
									                                        <%
									                                        String itemNow = g.getItem(i);
									                                        String[] items = itemNow.split(" ");
									                                        itemNow = "";
									                                        for(String item1 : items){
									                                        	itemNow += item1 + "-";	
									                                        }
									                                        %>
									                                        	onclick=<%= "removeFromList(" + index + "," + "\"" + itemNow + "\"" + ")"%>>
									                                            <i style="color:white" class="fas fa-times"></i>
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
                               <option value="grocery">Groceries</option>
                           </select>
                       </div>
                   </li>
                   <li class="nav-item mb-3">
                       <input id="manage_lists_btn" class="btn btn-secondary" onclick=<%="redirectToManageList("+ "\"" + index + "\"" +  ")"%> type="button" value="Manage Lists">
                   </li>
                   <li class="nav-item">
                       <a class="btn btn-secondary" onclick=<%= "redirectToRecipe(\"" + "/ImHungry/ResultsPageController?action=search&term="+ term + "&index=" + index + "&token=" + token + "\")" %>>Back to Search</a>
                   </li>
                   <li class="nav-item my-3">
                       <a class="btn btn-secondary" onclick=<%= "redirectToRecipe(\"" + "/ImHungry/RedirectionController?action=results&term="+ term +"&index=" + index + "&token=" + token + "\")" %>>Back to Results</a>
                   </li>
               </ul>
           </div>
       	</div>
      </div>
      
      <!-- Movable list reordering javascript -->
      <script type="text/javascript">
      	console.log('reordering entered')
		var list = document.getElementById('movable-list');
		var sortable = Sortable.create(list, {
			handle: '.movable-list-handle',
			animation: 150
		});
      </script>
	
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
	
		(ns)?window.onMouseMove=Mouse:document.onmousemove=Mouse;
	
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