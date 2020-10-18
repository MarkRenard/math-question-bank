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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.UnavailableException;

public class ProblemDao {

	private Connection connection;
	
	private PreparedStatement results;			// The default statement that selects all the entries
	private PreparedStatement problems;			// Statement selecting problems with specified cid
	private PreparedStatement search;			// Statement selecting problems with matching associated keywords
	
	private PreparedStatement newQuestion;		// Statement that inserts a new question into 'problem'
	private PreparedStatement newKeyword;		// statement that inserts a new keyword into 'keywords'
	private PreparedStatement assignCategory;	// Statement that inserts an entry into 'contains'
	private PreparedStatement associateKeyword;	// Statement that associates a keyword with a problem id
	
	private PreparedStatement matchingProblems;		// Statement selecting problems with the same content
	private PreparedStatement matchingAssignment;	// Statement selecting matching entries in 'contains'
	private PreparedStatement matchingAssociation;	// Statement selecting matching entries in 'associated'
	
	private PreparedStatement matchingKidInKeywordTable; // Statement selecting keyword id with matching 'keyword'


	public ProblemDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathprobdb1", "root", "");
			
			//createTables(); // Creates the tables 'contains' and 'category'
			
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
			
			// Statement selecting problems with matching associated keywords
			search = connection.prepareStatement(
					"SELECT P.pid, content, order_num, C.cid " +
					"FROM problem P " +
					"LEFT OUTER JOIN contains C ON P.pid = C.pid " +
					"JOIN associated A on A.pid = P.pid " +
					"JOIN keyword K on K.kid = A.kid " +
					"WHERE K.keyword = ?");

			
			// Prepares statement that enters a new problem into the database
			newQuestion = connection.prepareStatement(
					"INSERT INTO problem (content) "
					+ "VALUES (?)");

			// Prepares statement that enters a new keyword into the database
			newKeyword = connection.prepareStatement(
					"INSERT INTO keyword (keyword) "
					+ "VALUES (?)");
			
			// Prepares statement that retrieves problems with matching content
			matchingProblems = connection.prepareStatement(
					"SELECT * FROM problem WHERE content = ?");
			
			// Statement selecting matching entries in 'contains'
			matchingAssignment = connection.prepareStatement(
					"SELECT * FROM contains WHERE cid = ? AND pid = ?");
			
			// Statement selecting matching entries in 'associated'
			matchingAssociation = connection.prepareStatement(
					"SELECT * FROM associated WHERE kid = ? AND pid = ?");
			
			// Prepares statement selecting matching keywords
			matchingKidInKeywordTable = connection.prepareStatement(
					"SELECT kid FROM keyword WHERE keyword = ? LIMIT 1");
			
			// Prepares statement that assigns a problem to a category
			assignCategory = connection.prepareStatement(
					"INSERT INTO contains (cid, pid) VALUES (?, ?)");
			
			// Prepares statement that associates a problem with a keyword
			associateKeyword = connection.prepareStatement(
					"INSERT INTO associated (kid, pid) VALUES (?, ?)");
			
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
	
	// Returns the results of a keyword search
	public List<Problem> getSearchResults(List<String> searchTerms) 
			throws SQLException {
		
		// Set of non-duplicate search results
		HashSet<Problem> searchResults = new HashSet<Problem>();
		ResultSet rs; // Results of executing each query
		
		// Adds the result of the query for each search term to searchResults
		for (String term : searchTerms) {
			term = term.replaceAll("\\s",  ""); // Removes spaces
			
			// Performs search query
			search.setString(1, term);
			rs = search.executeQuery();
			
			// Adds the results
			addResultSetToProblemCollection(rs, searchResults);
		}
		
		return new ArrayList<Problem>(searchResults);
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
	
	private void addResultSetToProblemCollection(ResultSet rs, Collection<Problem> collection) {
		try {

			while (rs.next()) {
				Problem prob = new Problem();

				// Sets properties of each problem bean
				prob.setPid(rs.getInt(1));
				prob.setContent(rs.getString(2));
				prob.setOrder_num(rs.getInt(3));
				prob.setCid(rs.getInt(4));

				// Adds each problem to the list
				collection.add(prob);
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}
	
	// This method adds a new question to the problem table
	public void addQuestion(String question) throws SQLException {
		if (question != null && question != "") {
			newQuestion.setString(1, question);
			newQuestion.executeUpdate();
		}
	}
	
	// Adds a new keyword to the keywords table, associates with pid in associated table
	public void addKeywords(List<String> keywords, int pid) throws SQLException {
		int kid; // Keyword id of each new keyword
		
		System.out.println("probdao: addKeywords EXECUTING!");
		if (keywords.size() < 1) return;
		System.out.println("probdao: keywords.size() > 0!!!");
			
		// int i = 0; i < keywords.size(); i++
		for (String keyword : keywords) {
			keyword = keyword.replaceAll("\\s",  ""); // Removes whitespace
			
			// Adds the new keyword to the keyword table if it doesn't exist already
			if (keywordIsUnique(keyword)) {
				System.out.println("probdao: adding keyword " + keyword);
				newKeyword.setString(1, keyword);
				newKeyword.executeUpdate();
			} else {
				System.out.println("probdao: keyword " + keyword + " is not unique!");
			}
			
			// Gets keyword id of keyword
			matchingKidInKeywordTable.setString(1, keyword);
			ResultSet rs = matchingKidInKeywordTable.executeQuery();
			rs.next();
			kid = rs.getInt(1);
			System.out.println("probdao - kid of keyword " + keyword +": " + kid);
			
			System.out.println("problemdao - kid: " + kid);
			System.out.println("problemdao - pid: " + pid);
			System.out.println("problemdao - unique: " + associationIsUnique(kid, pid));
			
			// Associates kid with pid in associated table if the relation hasn't already been added
			if (associationIsUnique(kid, pid)) {
				associateKeyword.setInt(1, kid);
				associateKeyword.setInt(2, pid);
				associateKeyword.executeUpdate();	
				System.out.println("probdao - association ADDED: " + kid + ", " + pid);
			} else {
				System.out.println("probdao - association is NOT unique: " + kid + ", " + pid);
			}
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
		
		return !rs.next(); // True if the result set is empty
	}
	
	// This method returns true if there is no matching category-problem mapping
	public boolean assignmentIsUnique(int cid, int pid) throws SQLException {
		matchingAssignment.setInt(1, cid);
		matchingAssignment.setInt(2, pid);
		ResultSet rs = matchingAssignment.executeQuery();
		
		return !rs.next(); // True if the result set is empty
	}
	
	// Returns true if there is no matching keyword-problem mapping
	public boolean associationIsUnique(int kid, int pid) throws SQLException {
		matchingAssociation.setInt(1, kid);
		matchingAssociation.setInt(2, pid);
		ResultSet rs = matchingAssociation.executeQuery();
		
		return !rs.next(); // True if the result set is empty
	}
	
	// Returns true if there is no matching keyword in the keyword table
	public boolean keywordIsUnique(String keyword) throws SQLException {
		matchingKidInKeywordTable.setString(1, keyword);
		ResultSet rs = matchingKidInKeywordTable.executeQuery();
		
		return !rs.next(); // True if the result set is empty
	}

}
