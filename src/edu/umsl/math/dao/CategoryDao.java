package edu.umsl.math.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umsl.math.beans.Category;

public class CategoryDao {
	
	private PreparedStatement categories;		// Statement selecting all entries in 'categories'
	private PreparedStatement newCategory;		// Statement that inserts a new category into 'category'
	private PreparedStatement matchingCategories;	// Statement selecting category with matching 'category_name'
	
	
	private Connection connection;
	
	public CategoryDao() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathprobdb1", "root", "");
		
		// Prepares statement that retrieves categories
		categories = connection.prepareStatement(
				"SELECT cid, category_name "
				+ "FROM category");
		
		newCategory = connection.prepareStatement(
				"INSERT INTO category (category_name) "
				+ " VALUES (?)");
		
		// Prepares statement that retrieves matching categories
		matchingCategories = connection.prepareStatement(
				"SELECT * FROM category WHERE category_name = ?");
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
	
	// This method adds a new category to the problem table
	public void addCategory(String category_name) throws SQLException {
		if (category_name != null && category_name != "") {
			newCategory.setString(1, category_name);
			newCategory.executeUpdate();
		}
	}
	
	// This method returns true if there is no category with a matching name
	public boolean categoryIsUnique(String categoryName) throws SQLException {
		matchingCategories.setString(1, categoryName);
		ResultSet rs = matchingCategories.executeQuery();
		
		return !rs.next(); // True if the result set is empty
	}

}
