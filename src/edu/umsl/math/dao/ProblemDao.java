package edu.umsl.math.dao;

import edu.umsl.math.beans.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.UnavailableException;

public class ProblemDao {

	private Connection connection;
	private PreparedStatement results;
	private PreparedStatement categories;
	private PreparedStatement newQuestion;
	private PreparedStatement newCategory;
	private PreparedStatement matchingProblems;
	private PreparedStatement matchingCategories;

	public ProblemDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathprobdb1", "root", "");
			
			// Prepares statement that retrieves problems
			results = connection.prepareStatement(
					"SELECT pid, content, order_num " 
					+ "FROM problem ORDER BY order_num DESC");
			
			// Prepares statement that retrieves categories
			categories = connection.prepareStatement(
					"SELECT cid, category_name "
					+ "FROM category");
			
			// Prepares statement that enters a new problem into the database
			newQuestion = connection.prepareStatement(
					"INSERT INTO problem (content) "
					+ "VALUES (?)");
			
			// Prepares statement that enters a new category into the database
			newCategory = connection.prepareStatement(
					"INSERT INTO category (category_name) "
					+ "VALUES (?)");
			
			// Prepares statement that retrieves problems with matching content
			matchingProblems = connection.prepareStatement(
					"SELECT * FROM problem WHERE content = ?");
			
			// Prepares statement that retrieves matching categories
			matchingCategories = connection.prepareStatement(
					"SELECT * FROM category WHERE category_name = ?");
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new UnavailableException(exception.getMessage());
		}

	}
	
	public List<Problem> getProblemList() throws SQLException {
		List<Problem> problist = new ArrayList<Problem>();
		
		try {
			ResultSet resultsRS = results.executeQuery();

			while (resultsRS.next()) {
				Problem prob = new Problem();

				prob.setPid(resultsRS.getInt(1));
				prob.setContent(resultsRS.getString(2));
				prob.setOrder_num(resultsRS.getInt(3));

				problist.add(prob);
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		
		return problist;
	}
	
	public List<Category> getCategoryList() throws SQLException {
		List<Category> categorylist = new ArrayList<Category>();
		
		System.out.println("getCategoryList executing!");
		
		try {
			ResultSet categoriesRS = categories.executeQuery();

			while (categoriesRS.next()) {
				Category category = new Category();

				category.setCid(categoriesRS.getInt(1));
				category.setCategoryName(categoriesRS.getString(2));

				System.out.print(category.getCid());
				System.out.print(category.getCategoryName());
				
				categorylist.add(category);
			}
			System.out.println("");
			
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		
		return categorylist;
	}	
	
	// This method adds a new question to the problem table
	public void addQuestion(String question) throws SQLException {
		if (question != null && question != "") {
			newQuestion.setString(1, question);
			newQuestion.executeUpdate();
		}
	}
	
	// This method adds a new category to the problem table
	public void addCategory(String category_name) throws SQLException {
		if (category_name != null && category_name != "") {
			newCategory.setString(1, category_name);
			newCategory.executeUpdate();
		}
	}
	
	// This method returns true if there is no problem with matching content
	public boolean problemIsUnique(String problemContent) throws SQLException {
		matchingProblems.setString(1, problemContent);
		ResultSet rs = matchingProblems.executeQuery();
		
		return !rs.next(); // False if the result set is non-empty
	}
	
	// This method returns true if there is no category with a matching name
	public boolean categoryIsUnique(String categoryName) throws SQLException {
		matchingCategories.setString(1, categoryName);
		ResultSet rs = matchingCategories.executeQuery();
		
		return !rs.next(); // False if the result set is non-empty
	}

}
