package cc.nicenight.ncrawler;

import java.util.Date;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class Task {
	public Task(String uRL, int deepth, int maxlinks, int maxpages, int maxlinkeachpage,
			String field, String pass, String search) {
		super();
		this.uRL = uRL;
		this.status = 0;
		this.maxdeepth = deepth;
		this.maxlinks = maxlinks;
		this.maxpages = maxpages;
		this.maxlinkeachpage = maxlinkeachpage;
		this.field = field;
		this.pass = pass;
		this.search = search;
	}

	public Task(int taskID, String uRL, int status, int deepth, int maxlinks, int maxpages,
			int maxlinkeachpage, String field, String pass, String search, Date createtime) {
		super();
		this.taskID = taskID;
		this.uRL = uRL;
		this.status = status;
		this.maxdeepth = deepth;
		this.maxlinks = maxlinks;
		this.maxpages = maxpages;
		this.maxlinkeachpage = maxlinkeachpage;
		this.field = field;
		this.pass = pass;
		this.search = search;
		this.createtime = createtime;
	}

	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getMaxlink() {
		return maxlinks;
	}

	public void setMaxlink(int maxlink) {
		this.maxlinks = maxlink;
	}

	public int getMaxpage() {
		return maxpages;
	}

	public void setMaxpage(int maxpage) {
		this.maxpages = maxpage;
	}

	public int getMaxlinkeachpage() {
		return maxlinkeachpage;
	}

	public void setMaxlinkeachpage(int maxlinkeachpage) {
		this.maxlinkeachpage = maxlinkeachpage;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getURL() {
		return uRL;
	}

	public void setURL(String uRL) {
		this.uRL = uRL;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDeepth() {
		return maxdeepth;
	}

	public void setDeepth(int deepth) {
		this.maxdeepth = deepth;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	int taskID = 0;
	String uRL = "";
	int status = 0;
	int maxdeepth = 0;
	int maxlinks = 0;
	int maxpages = 0;
	int maxlinkeachpage = 0;
	String field = "";
	String pass = "";
	String search = "";
	Date createtime = new Date();
}