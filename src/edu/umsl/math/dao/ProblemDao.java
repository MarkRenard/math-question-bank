/** ProblemsDao was created by Dr. He and modified by Mark Renard on 11/16/2019.
 * 
 *  This file defines a database access object that enables interaction with a
 *  database called mathprobdb1, assuming it exists, is accessible via
 *  //localhost:3306/mathprobdb1, and was created using problem.sql in the files
 *  page on canvas.
 */

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
	
	private PreparedStatement results;				// The default statement that selects all the entries
	private PreparedStatement problems;				// Statement selecting problems with specified cid
	private PreparedStatement categories;			// Statement selecting all entries in 'categories'
	
	private PreparedStatement newQuestion;			// Statement that inserts a new question into 'problem'
	private PreparedStatement newCategory;			// Statement that inserts a new category into 'category'
	private PreparedStatement assignCategory;		// Statement that inserts an entry into 'contains'
	
	private PreparedStatement matchingProblems;		// Statement selecting problems with the same content
	private PreparedStatement matchingCategories;	// Statement selecting category with matching 'category_name'
	private PreparedStatement matchingAssignment;	// Statement selecting matching entries in 'contains'


	public ProblemDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathprobdb1", "root", "");
			
			createTables(); // Creates the tables 'contains' and 'category'
			
			// Prepares statement that retrieves problems and category ids
			results = connection.prepareStatement(
					"SELECT P.pid, content, order_num, cid " +
					"FROM problem P " +
					"LEFT OUTER JOIN contains C ON P.pid = C.pid " +
					"ORDER BY order_num");
			
			// Prepares statement that retrieves problems and category ids in a particular category			
			problems = connection.prepareStatement(
					"SELECT P.pid, content, order_num, cid " +
					"FROM problem P " +
					"LEFT OUTER JOIN contains C ON P.pid = C.pid " +
					"WHERE cid = ? " +
					"ORDER BY order_num");
			
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
			
			matchingAssignment = connection.prepareStatement(
					"SELECT * FROM contains WHERE cid = ? AND pid = ?");
			
			// Prepares statement that assigns a problem to a category
			assignCategory = connection.prepareStatement(
					"INSERT INTO contains (cid, pid) values (?, ?)");
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new UnavailableException(exception.getMessage());
		}

	}
	
	// Creates tables category and contains if the don't exist already
	private void createTables() throws SQLException {
		// Creates tables `category`
		PreparedStatement createCategoryTable = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS `category` ( " +
			    "`cid` int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
			    "`category_name` varchar(256))");
		createCategoryTable.execute();
		
		// Creates table `contains`
		PreparedStatement createContainsTable = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS `contains` (" + 
				"    `cid` int unsigned NOT NULL, " + 
				"    `pid` int unsigned NOT NULL, " + 
				"    FOREIGN KEY (`cid`) REFERENCES `category`(`cid`), " + 
				"    FOREIGN KEY (`pid`) REFERENCES `problem` (`pid`))");
		createContainsTable.execute();
	}
	
	// Returns a list of problems with matching cid
	public List<Problem> getProblemList(int cid) throws SQLException {
		List<Problem> problist = new ArrayList<Problem>();
		PreparedStatement statement;
		
		// Selects query based on argument
		if (cid < 1) {
			statement = results;
		} else {
			statement = problems;
			statement.setInt(1, cid);
		}
		
		// Retrieves problem list
		try {
			ResultSet resultsRS = statement.executeQuery();

			while (resultsRS.next()) {
				Problem prob = new Problem();

				// Sets properties of each problem bean
				prob.setPid(resultsRS.getInt(1));
				prob.setContent(resultsRS.getString(2));
				prob.setOrder_num(resultsRS.getInt(3));
				prob.setCid(resultsRS.getInt(4));

				// Adds each problem to the list
				problist.add(prob);
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		
		return problist;
	}
	
	// Returns all of the categories in the table 'contains'
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
	
	// This method inserts an entry with the passed cid and pid into 'contains'
	public void assignCategoryToProblem(int cid, int pid) throws SQLException {
		if (assignmentIsUnique(cid, pid)) {
			assignCategory.setInt(1, cid);
			assignCategory.setInt(2, pid);
			assignCategory.executeUpdate();
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
	
	// This method returns true if there is no category with a matching name
	public boolean assignmentIsUnique(int cid, int pid) throws SQLException {
		matchingAssignment.setInt(1, cid);
		matchingAssignment.setInt(2, pid);
		ResultSet rs = matchingAssignment.executeQuery();
		
		return !rs.next(); // False if the result set is non-empty
	}

}
