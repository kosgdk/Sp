<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-default">
		<div class="container-fluid">
		
			<div class="navbar-header">
      			<a class="navbar-brand" href='<spring:url value="/"/>'>СП Gillette</a>
    		</div>
    		
    		<ul class="nav navbar-nav">
    		
    			<li><a href='<spring:url value="/"/>'>Home</a></li>
    		
    			<li class="dropdown">
          			
          			<a href="#" class="dropdown-toggle" 
          				data-toggle="dropdown" role="button" 
          				aria-expanded="false">СП <span class="caret"></span></a>
          	
          			<ul class="dropdown-menu" role="menu">
            			<li><a href='<spring:url value="/createsp"/>'>Создать новое СП</a></li>
            			<li><a href='<spring:url value="/addposition"/>'>Добавить позицию</a></li>
						<li><a href='<spring:url value="/createclient"/>'>Добавить клиента</a></li>
          			</ul>
          			
        		</li>
        		
    			<li class="dropdown">
          		
          			<a href="#" class="dropdown-toggle" 
          				data-toggle="dropdown" role="button" 
          				aria-expanded="false">Resources <span class="caret"></span></a>
          		
          			<ul class="dropdown-menu" role="menu">
            			<li><a href="#">Add</a></li>
            			<li><a href="#">Find</a></li>
          			</ul>
        		
        		</li>
        		
    		</ul>
    		
		</div>
</nav>