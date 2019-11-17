package edu.umsl.math.web;

import java.io.IOException;
import java.util.List;

import edu.umsl.math.beans.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.ProblemDao;

/**
 * Servlet implementation class ListMathServlet
 */
@WebServlet("/listmath")
public class ListMathServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String errorMsg = null;  // Message to display on error
		ProblemDao probdao = null;  // Problem database access object
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
		
		// Gets a new question from the request if one exists
		String newQuestion = (String) request.getParameter("new-question");

		
		// Gets a new category from the request if one exists
		String newCategory = (String) request.getParameter("new-category");

		
		try {
			probdao = new ProblemDao();
			
			// Adds a new question to the database if it's nonempty and unique
			if(problemIsValid(newQuestion)) {
				if (probdao.problemIsUnique(newQuestion)) {
					probdao.addQuestion(newQuestion);
				} else {
					errorMsg = "Error: The problem \"" 
							+ newQuestion + "\" already exists.";
				}
			}
			
			// Adds a new category to the database if it's nonempty and unique
			if(categoryIsValid(newCategory)) {
				if (probdao.categoryIsUnique(newCategory)) {
					probdao.addCategory(newCategory);
				} else {
					errorMsg = "Error: The category \"" 
							+ newCategory + "\" already exists.";
				}
			}
			
			// Retrieves the list of problems from the database
			List<Problem> problist = probdao.getProblemList();
			
			// Retrieves the list of categories from the database
			List<Category> categorylist = probdao.getCategoryList();
			
			// Print statements for debugging
			System.out.println("New question: " + newQuestion);
			System.out.println("New category: " + newCategory);
			System.out.println("Problem list: " + problist);
			System.out.println("Category list: " + categorylist);
			
			// Sets attributes in request object
			request.setAttribute("problist", problist);
			request.setAttribute("categorylist", categorylist);
			request.setAttribute("errormsg", errorMsg);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	// Validates the content of a new problem
	private boolean problemIsValid(String str) {
		return (str != null && str != "");
	}
	
	// Validates the string for a new category
	private boolean categoryIsValid(String str) {
		return (str != null && str != "");
	}

}
