package cc.nicenight.ncrawler;

import java.util.Date;
/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class Task {
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
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeepth() {
        return deepth;
    }

    public void setDeepth(int deepth) {
        this.deepth = deepth;
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

    int taskID;
    String URL;
    int status;
    int deepth;
    int maxlinks;
    int maxpages;
    int maxlinkeachpage;
    String field;
    String pass;
    String search;
    Date createtime;
}