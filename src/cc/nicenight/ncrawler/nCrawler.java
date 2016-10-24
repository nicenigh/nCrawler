package cc.nicenight.ncrawler;

/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class nCrawler {
    public static void main(String args[]) throws Exception {
        String frontpage = "http://news.163.com/";

        SQLiteHelper db = new SQLiteHelper("crawler.db");
        // connect the sqlite database
        // create database and tables that will be needed
        if (db.connect()) {
            System.out.println("connection built");
            int count = 0;
            int maxdeepth = 3;
            int deepth = 0;

            int task = db.instertTask(frontpage, maxdeepth, "");
            int id = db.instertRecord(task, frontpage, 0);

            String url = frontpage;

            // crawl every link in the database
            while (true) {
                // get page content of link "url"
                // new crawlThread(id, db).start();
                httpGet.getByString(id, db);
                db.crawledRecord(id);
                count++;

                // set boolean value "crawled" to true after crawling this page
                id = db.nextRecord(maxdeepth);

                // set a limit of crawling count
                if (count > 10 || url == null) {
                    break;
                }
            }
            db.close();

            System.out.println("Done.");
            System.out.println(count);
        }
        System.exit(0);
    }

    static class crawlThread extends Thread {
        private int id;
        private SQLiteHelper db;

        public crawlThread(int id, SQLiteHelper db) {
            this.id = id;
            this.db = db;
        }

        public void run() {
            try {
                httpGet.getByString(id, db);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
