package cc.nicenight.ncrawler;

import java.util.Date;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class Record {
	public Record(int taskID, String uRL, int priority, int deepth) {
		super();
		this.taskID = taskID;
		this.uRL = uRL;
		this.priority = priority;
		this.deepth = deepth;
	}

	public Record(int recordID, int taskID, String uRL, String title, String author,
			String content, int priority, int deepth, int type, Date crawltime, int status) {
		super();
		this.recordID = recordID;
		this.taskID = taskID;
		this.uRL = uRL;
		this.title = title;
		this.author = author;
		this.content = content;
		this.priority = priority;
		this.deepth = deepth;
		this.type = type;
		this.crawltime = crawltime;
		this.status = status;
	}

	public Record() {
		super();
		// TODO Auto-generated constructor stub
	}

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
		return uRL;
	}

	public void setURL(String uRL) {
		this.uRL = uRL;
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
		return status;
	}

	public void setCrawled(int crawled) {
		this.status = crawled;
	}

	int recordID = 0;
	int taskID = 0;
	String uRL = "";
	String title = "";
	String author = "";
	String content = "";
	int priority = 0;
	int deepth = 0;
	int type = 0;
	Date crawltime = new Date();
	int status = 0;
}