<!-- 
	 list.jsp was created by Dr. He and modified by Mark Renard on 11/16/2019.
	 
	 This jsp file displays a table of math problems and allows the user to
	 categorize questions and enter new problems and categories.
-->

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
					<table width="100%" class="table table-bordered table-striped">
					
						<!-- Top-of-page form elements -->
						<tr>
							<td colspan="3">
								<table width="100%">
									<tr> ${requestScope.errormsg} </tr>
									
									<!-- Keyword search form -->
									<form id="new-keywords-form" action="listmath" method="GET">
										<tr>
											<td width="70%" class="text-center">
												<input id="keyword-search-text" type="text" class="form-control" name="keyword-search"/>
											</td>
											<td width="30%">
												<input id="keyword-search-submit" type="submit" class="form-control" value="Keyword Search" />		
											</td>
										</tr>
									</form>
									
									<!-- Drop-down list of categories for display -->
									<tr>
										<form action="listmath" method="GET">
											<td>
												<select name="display-category" size=1>
													<option value="0" disabled selected>Select a Category</option>
													<option value="0"> All </option>
													<% for (Category category : mycategorylist) { %>
												 			<option value="<%=category.getCid() %>"><%=category.getCategoryName() %></option>
												 	<% } %>
												
												</select>
											</td>
											<td>
												<input type="submit" class="form-control" value="Display Category" />												
											</td>
										</form>
									</tr>
									
									<!-- New question form -->
									<form action="listmath" method="GET">
										<tr>
											<td width="70%" class="text-center">
												<input type="text" class="form-control" name="new-question"/>
											</td>
											<td width="30%">
												<input type="submit" class="form-control" value="Enter Question" />		
											</td>
										</tr>
									</form>
										
									<!--  New category form -->
									<form action="listmath" method="GET">
										<tr>
											<td width="70%" class="text-center">
												<input type="text" class="form-control" name="new-category"/>
											</td>
											<td width="30%">
												<input type="submit" class="form-control" value="Enter Category" />		
											</td>
										</tr>
									</form>
									
									<!-- Drop-down list of categories for assignment -->
									<tr>
										<td>
											<select name="assignment-category" size=1>
												<option value="" disabled selected>Select a Category</option>
												<% for (Category category : mycategorylist) { %>
											 			<option value="<%=category.getCid() %>"><%=category.getCategoryName() %></option>
											 	<% } %>										
											</select>
										</td>
										<td>
											<div id="assigment-label">Assign to Category </div>
										</td>
									</tr>
									
									<!--  New keywords form -->
									<form id="new-keywords-form" action="listmath" method="GET">
										<tr>
											<td width="70%" class="text-center">
												<input id="keywords-text" type="hidden" class="form-control" name="new-keywords"/>
											</td>
											<td width="30%">
												<input id="keywords-submit" type="hidden" class="form-control" value="Enter Keywords" />		
											</td>
											<input id="keywords-hidden-form-element" type="hidden" name="keywords-pid"/>
										</tr>
									</form>
								</table>
							</td>
						</tr>
						
						<!-- Category assignment form -->
						<form action="listmath" method="GET">

							<!-- Hidden form elements -->
							<input type="hidden" id="assignment-cid" name="assignment-cid">
							<input type="hidden" id="assignment-pid" name="assignment-pid">
							<input type="hidden" id="keywords-pid" name="keywords-pid">
						
							<!-- Table of problems -->
							<%
								for (Problem prob : myproblist) {
							%>
							<tr class="problem-row" cid="<%=prob.getCid() %>">
								<!-- pid column -->
								<td id="pid<%=prob.getPid()%>" width="8%" class="text-center"><%=prob.getPid()%></td>
								
								<!-- Problem content column -->
								<td width="76%"><%=prob.getContent()%></td>
								
								<!-- Assignment button column -->
								<td width="8%">
									<button type="button" class="btn btn-default btn-sm assign-button" style="display:none" cid="<%=prob.getCid() %>" pid="<%=prob.getPid() %>">
						        		<span class="glyphicon glyphicon-plus assign-glyphicon" style="display:none" cid="<%=prob.getCid() %>" pid="<%=prob.getPid() %>">Assign</span>
						        	</button>
									<span class="glyphicon glyphicon-check added-glyphicon" style="display:none" cid="<%=prob.getCid() %>" pid="<%=prob.getPid() %>">Added</span>
								</td>
								
								<!-- Keyword button column -->
								<td width="8%">
									<button type="button" class="btn btn-default btn-sm keywords-button" style="display:inline" cid="<%=prob.getCid() %>" pid="<%=prob.getPid() %>">
						        		<span class="glyphicon glyphicon-plus keywords-glyphicon" style="display:inline" pid="<%=prob.getPid() %>">Keywords</span>
						        	</button>
									<span class="glyphicon glyphicon-check added-glyphicon" style="display:none" pid="<%=prob.getPid() %>">Add</span>
								</td>
							</tr>
							<%
								}
							%>
						</form>
				</table>
				
			</div>
		</div>
	</div>
</body>
</html>