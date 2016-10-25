package cc.nicenight.ncrawler;

import java.util.Date;
/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class Record {
    public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDeepth() {
        return deepth;
    }

    public void setDeepth(int deepth) {
        this.deepth = deepth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCrawltime() {
        return crawltime;
    }

    public void setCrawltime(Date crawltime) {
        this.crawltime = crawltime;
    }

    public int getCrawled() {
        return crawled;
    }

    public void setCrawled(int crawled) {
        this.crawled = crawled;
    }

    int recordID;
    int taskID;
    String URL;
    String title;
    String author;
    String content;
    int priority;
    int deepth;
    int type;
    Date crawltime;
    int crawled;
}