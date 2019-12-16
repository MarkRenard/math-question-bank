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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cid;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + order_num;
		result = prime * result + pid;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if (cid != other.cid)
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (order_num != other.order_num)
			return false;
		if (pid != other.pid)
			return false;
		return true;
	}
}
