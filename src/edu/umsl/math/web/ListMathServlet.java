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
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
		
		String newQuestion = (String) request.getParameter("new-question");
		System.out.println(newQuestion);
		
		try {
			ProblemDao probdao = new ProblemDao();
			
			// Adds a new question to the database if one was set
			probdao.addQuestion(newQuestion);
			
			// Retrieves the list of problems from the database
			List<Problem> problist = probdao.getProblemList();
			request.setAttribute("problist", problist);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
