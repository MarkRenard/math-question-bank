/** ListMathServlet.java was created by Dr. He and modified by Mark Renard on 11/16/2019.
 * 
 * This servlet interacts with the database mathprobdb1 via a ProblemDao object
 * and forwards tabular data to list.jsp, where it is displayed.
 */

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
 * 
 */
@WebServlet("/listmath")
public class ListMathServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newQuestion;			// Stores a new question from the request if one exists
		String newCategory;			// Stores a new category from the request if one exists
		String newKeywords;			// String of new comma separated keywords, if they exist
		
		int assignmentPid = -1;		// The problem id for an assignment operation
		int assignmentCid = -1;		// The category id for an assignment operation
		int keywordsPid = -1;		// The problem id for a new keywords operation
		int displayCategory;		// Stores the category of problem to be displayed
		
		String errorMsg = null;  	// Message to display on error
		ProblemDao probdao = null;  // Problem database access object
		
		System.out.println("listmath servlet running!");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
		
		// Retrieves new questions, categories, or keywords
		newQuestion = (String) request.getParameter("new-question");
		newCategory = (String) request.getParameter("new-category");
		newKeywords = (String) request.getParameter("new-keywords");
		
		// Retrieves new problem IDs and category IDs from the request and perform validation
		assignmentPid = validatedId((String) request.getParameter("assignment-pid"));
		assignmentCid = validatedId((String) request.getParameter("assignment-cid"));
		keywordsPid = validatedId((String) request.getParameter("keywords-pid"));
		
		displayCategory = validatedId((String)request.getParameter("display-category"));
		
			
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
			
			// Performs category assignment if a category has been assigned
			if (assignmentPid > 0 && assignmentCid > 0) {
				probdao.assignCategoryToProblem(assignmentCid, assignmentPid);
			}
			
			// Retrieves lists from the database
			List<Problem> problist = probdao.getProblemList(displayCategory);
			List<Category> categorylist = probdao.getCategoryList();
			
			// Print statements for debugging
			System.out.println("New question: " + newQuestion);
			System.out.println("New category: " + newCategory);
			System.out.println("Display category: " + displayCategory);
	
			System.out.println("Assignment pid: " + assignmentPid);
			System.out.println("Assignment cid: " + assignmentCid);
			
			System.out.println("Problem list: " + problist);
			System.out.println("Category list: " + categorylist + "\n");
			
			// Sets attributes in request object
			request.setAttribute("problist", problist);
			request.setAttribute("categorylist", categorylist);
			request.setAttribute("errormsg", errorMsg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	// Returns a parsed int or -1 if the ID is invalid
	private static int validatedId(String idString) {
		int idInt = -1;
		try {
			idInt = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			System.out.println("Can't convert ID: " + idString);
		}
		
		return idInt;
	}
	
	// Validates the content of a new problem
	private static boolean problemIsValid(String str) {
		return (str != null && str != "");
	}
	
	// Validates the string for a new category
	private static boolean categoryIsValid(String str) {
		return (str != null && str != "");
	}

}
