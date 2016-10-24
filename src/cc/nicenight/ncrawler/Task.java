package cc.nicenight.ncrawler;

import java.util.Date;
/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class Task {
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
    String domain;
    String pass;
    String search;
    Date createtime;
}