/** ListMathServlet.java was created by Dr. He and modified by Mark Renard on 11/16/2019.
 * 
 * This servlet interacts with the database mathprobdb1 via a ProblemDao object
 * and forwards tabular data to list.jsp, where it is displayed.
 */

package edu.umsl.math.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.umsl.math.beans.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.CategoryDao;
import edu.umsl.math.dao.ProblemDao;

/**
 * Servlet implementation class ListMathServlet
 * 
 */
@WebServlet("/listmath")
public class ListMathServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String newQuestion;			// Stores a new question from the request if one exists
		String newCategory;			// Stores a new category from the request if one exists
		String newKeywords;			// String of new comma separated keywords, if they exist
		String keywordSearch;		// Keyword search query
		
		int assignmentPid = -1;		// The problem id for an assignment operation
		int assignmentCid = -1;		// The category id for an assignment operation
		int keywordsPid = -1;		// The problem id for a new keywords operation
		int displayCategory;		// Stores the category of problem to be displayed
		
		String errorMsg = null;  	// Message to display on error
		ProblemDao probdao = null;  // Problem database access object
		CategoryDao catdao = null;  // Category database access object
		
		List<String> newKeywordsList;	// Keywords to be add to the database
		List<Problem> problist;			// Problems to display
		List<Category> categorylist;	// Categories to display in the categories drop-down menu
		
		// Initializes lists
		newKeywordsList = new ArrayList<String>();
		problist = new ArrayList<Problem>();
		categorylist = new ArrayList<Category>();
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
		
		// Retrieves new questions, categories, keywords, or search terms
		newQuestion = (String) request.getParameter("new-question");
		newCategory = (String) request.getParameter("new-category");
		newKeywords = (String) request.getParameter("new-keywords");
		keywordSearch = (String) request.getParameter("keyword-search");
		
		// Retrieves new problem IDs and category IDs from the request and perform validation
		assignmentPid = validatedId((String) request.getParameter("assignment-pid"));
		assignmentCid = validatedId((String) request.getParameter("assignment-cid"));
		keywordsPid = validatedId((String) request.getParameter("keywords-pid"));
		displayCategory = validatedId((String)request.getParameter("display-category"));
			
		try {
			probdao = new ProblemDao();
			catdao = new CategoryDao();
			
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
				if (catdao.categoryIsUnique(newCategory)) {
					catdao.addCategory(newCategory);
				} else {
					errorMsg = "Error: The category \"" 
							+ newCategory + "\" already exists.";
				}
			}
			
			// Performs category assignment if a category has been assigned
			if (assignmentPid > 0 && assignmentCid > 0) {
				probdao.assignCategoryToProblem(assignmentCid, assignmentPid);
			}
			
			// Adds new keywords to the database, associates with pid
			if (keywordsAreValid(newKeywords)) {
				newKeywordsList = Arrays.asList(newKeywords.split(","));
				probdao.addKeywords(newKeywordsList, keywordsPid);
			}
			
			// Retrieves problem list
			if (keywordSearch != null && keywordSearch != "") {
				
				// Retrieves search results if a search query was entered
				List<String> searchTerms = Arrays.asList(keywordSearch.split(","));
				problist = probdao.getSearchResults(searchTerms);
			} else {
				
				// Retrieves problems in a category if one was selected, all problems otherwise
				problist = probdao.getProblemList(displayCategory);
			}
			
			// Retrieves the list of categories from the database
			categorylist = catdao.getCategoryList();
			
			// Print statements for debugging
			System.out.println("New question: " + newQuestion);
			System.out.println("New category: " + newCategory);
			System.out.println("Display category: " + displayCategory);
			System.out.println("New keywords: " + listToString(newKeywordsList));
	
			System.out.println("\nAssignment pid: " + assignmentPid);
			System.out.println("Assignment cid: " + assignmentCid);
			
			System.out.println("\nProblem list: " + problist);
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
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
	
	// Validates the string of new keywords
	private static boolean keywordsAreValid(String str) {
		return (str != null && str != "");
	}
	
	// Returns comma separated string from a list
	private static <T> String listToString (List<T> list){
		if (list.size() < 1) return "";
		
		StringBuilder strb = new StringBuilder((String) list.get(0) );
		for(int i = 1; i < list.size(); i++) {
			strb.append(", ");
			strb.append((String) list.get(i));
		}
		
		return strb.toString();
	}

}
