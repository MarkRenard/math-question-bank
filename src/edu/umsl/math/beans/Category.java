/** This bean stores a category of math problem **/

package edu.umsl.math.beans;

public class Category {
	private int cid; // A unique category identifier
	private String categoryName; // The name of the category
	
	public int getCid() { return cid; }
	public String getCategoryName() { return categoryName;}
	
	public void setCid(int newCid) { cid = newCid; }
	public void setCategoryName(String newName) { categoryName = newName; }

}
