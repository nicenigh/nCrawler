package cc.nicenight.ncrawler;

import java.net.URL;

import com.sun.org.apache.xpath.internal.operations.And;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class nCrawler {
	public static void main(String args[]) throws Exception {
		String frontpage = "http://news.163.com/";

		SQLiteHelper db = new SQLiteHelper("crawler.db");
		// connect the sqlite database
		// create database and tables that will be needed
		if (db.connect() != null) {
			db.dropTable();
			db.createTable();
			System.out.println("connection built");
			int count = 0;

			URL url = new URL(frontpage);

			Task task = new Task(frontpage, 3, 30, 10, 10, url.getHost(), "", "");
			int taskid = db.instertTask(task);

			Record record = new Record(taskid, frontpage, 0, 0);
			int id = db.instertRecord(record);

			String strurl = frontpage;

			task = db.getTaskbyID(taskid);

			// crawl every link in the database
			while (true) {
				// get page content of link

				db.crawlingRecord(id);
				record = db.getRecordbyID(id);

				// check limit of crawling count
				if (db.exeQrInt("select count(*) from record where taskID=" + task.taskID
						+ " and status<2 and deepth<" + task.maxdeepth + ";") < 1)
					break;
				if (db.exeQrInt("select count(*) from record where taskID=" + task.taskID
						+ " and status=2;") > task.maxlinks)
					break;

				if ((id > 0) && (record.deepth < task.maxdeepth)) {

					System.out.println("crawling:" + record.uRL);
					// multi thread
					new crawlThread(record.uRL).start();
					// single thread
					// httpGet.getByString(id, db);

					// set boolean value "crawled" to true after crawling this
					// page
					count++;

					// find next un-crawled page
					id = db.exeQrInt("select recordID from record where taskID=" + task.taskID
							+ " and status=0 and deepth<" + task.maxdeepth + ";");

					strurl = record.uRL;
				}

				// sleep for 10ms
				Thread.sleep(10);
			}
			db.close();

			System.out.println("Done.");
			System.out.println(count);
		}
		System.exit(0);
	}

	static class crawlThread extends Thread {
		private String strurl;

		public crawlThread(String strurl) {
			this.strurl = strurl;
		}

		public void run() {
			try {
				httpGet.getByString(strurl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
