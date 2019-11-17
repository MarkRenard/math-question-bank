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
	private PreparedStatement newQuestion;

	public ProblemDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathprobdb1", "root", "");
			
			// Prepares statement that retrieves database entries
			results = connection.prepareStatement(
					"SELECT pid, content, order_num " 
					+ "FROM problem ORDER BY order_num DESC");
			
			// Prepares statement that enters a new problem into the database
			newQuestion = connection.prepareStatement(
					"INSERT INTO problem (content) "
					+ "VALUES (?)");
			
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
	
	// This method adds a new question to the problem table if it's not empty
	public boolean addQuestion(String question) throws SQLException {
		if (question != null && question != "") {
			newQuestion.setString(1, question);
			newQuestion.execute();
			return true;
		}
		
		return false;
	}

}
