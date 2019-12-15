/** Problem.java was created by Dr. He and modified by Mark Renard on 11/16/2019.
 * 
 *  This class defines a bean designed to store problems from the database
 *  mathprobdb1 as defined in problem.sql in the files section on canvas.
 */

package edu.umsl.math.beans;

public class Problem {
	
	private int pid;
	private String content;
	private int order_num;
	private int cid;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
	
	public int getCid() {
		return cid;
	}
	
	public void setCid(int cid) {
		this.cid = cid;
	}

	public boolean equals(Problem prob) {
		return this.pid == prob.pid
				&& this.content == prob.content
				&& this.order_num == prob.order_num
				&& this.cid == prob.cid;
	}
}
