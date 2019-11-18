<%@ page import="java.util.*"%>
<%@ page import="edu.umsl.math.beans.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- Latest MathJacks script -->	
<script type="text/javascript"
	src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
<script type="text/javascript">
	window.MathJax = {
		tex2jax : {
			inlineMath : [ [ '$', '$' ], [ "\\(", "\\)" ] ],
			processEscapes : true
		}
	};
</script>

<!-- CSS to hide Assign buttons by default -->
<!-- <link rel="stylesheet" type="text/css" href="css/AssignButtons.css"> -->

<!-- JavaScript file that shows assign buttons for problems NOT in selected category -->
<script src="js/Assignment.js"></script>

<title>Math Question Bank</title>
</head>
<body>
	<%
		List<Problem> myproblist = (List<Problem>) request.getAttribute("problist");
		List<Category> mycategorylist = (List<Category>) request.getAttribute("categorylist");
	%>
	<div class="container">
		<div class="row">
			<div class="col-md-offset-2 col-md-8">
			
				<!-- Buttons in form submit requests to servlet -->
				<form action="listmath" method="POST">
					<table width="100%" class="table table-bordered table-striped">
					
						<!-- Top-of-page form elements -->
						<tr>
							<td colspan="3">
								<table width="100%">
									<tr> ${requestScope.errormsg} </tr>
								
									<!--  New question form elements -->
									<tr>
										<td width="70%" class="text-center">
											<input type="text" class="form-control" name="new-question"/>
										</td>
										<td width="30%">
											<input type="submit" class="form-control" value="Enter Question" />		
										</td>
									</tr>
									
									<!--  New category form elements -->
									<tr>
										<td width="70%" class="text-center">
											<input type="text" class="form-control" name="new-category"/>
										</td>
										<td width="30%">
											<input type="submit" class="form-control" value="Enter Category" />		
										</td>
									</tr>
									
									<!-- Drop-down list of categories for assignment -->
									<tr>
										<div id="assigment-label">Assign to Category </div>
										<select name="assignment-category" size=1>
											<option value="" disabled selected>Select a Category</option>
											<% for (Category category : mycategorylist) { %>
										 			<option value="<%=category.getCid() %>"><%=category.getCategoryName() %></option>
										 	<% } %>
										
										</select>
									</tr>
									
								</table>
							</td>
						</tr>
						<!-- Table of problems -->
						<%
							for (Problem prob : myproblist) {
						%>
						<tr>
							<!-- pid column -->
							<td id="pid<%=prob.getPid()%>" width="8%" class="text-center"><%=prob.getPid()%></td>
							
							<!-- Problem content column -->
							<td width="84%"><%=prob.getContent()%></td>
							
							<!-- Button column -->
							<td width="8%">
								<button type="button" class="btn btn-default btn-sm assign-button" style="display:none" cid="${prob.cid}">
					        	<span class="glyphicon glyphicon-plus assign-glyphicon" style="display:none" cid="${prob.cid}">Assign</span>
					        </button>
							
							</td>
						</tr>
						<%
							}
						%>
					</table>
				</form>
				
			</div>
		</div>
	</div>
</body>
</html>