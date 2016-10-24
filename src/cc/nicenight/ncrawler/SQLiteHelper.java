package cc.nicenight.ncrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class SQLiteHelper {

    private static Connection conn = null;
    private String dbfile;
    private String sql = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private PreparedStatement insert_task = null;
    private PreparedStatement insert_record = null;
    private PreparedStatement insert_keyword = null;
    private PreparedStatement insert_index = null;
    private PreparedStatement update_task = null;
    private PreparedStatement update_record = null;

    public SQLiteHelper(String dbfile) {
        this.dbfile = dbfile;
        connect();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        dropTable();
        createTable();
        try {
            insert_task = conn
                    .prepareStatement("insert into task (URL, status, deepth, domain, pass, search, createtime) values (?,?,?,?,?,?,datetime('now'));");
            insert_record = conn
                    .prepareStatement("insert into record (taskID, URL, title, author, content, deepth, type, crawled, crawltime) values (?,?,?,?,?,?,?,?,datetime('now'));");
            insert_keyword = conn.prepareStatement("insert into keyword (keyword) values (?);");
            insert_index = conn.prepareStatement("insert into kwindex (recordID, keywordID) values (?,?);");
            update_task = conn.prepareStatement("update task set status=? where taskID=?;");
            update_record = conn.prepareStatement("update record set title=?, author=?, content=?, crawltime=?, crawled=? where recordID=?;");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SQLiteHelper() {
    }

    public boolean connect() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String dburl = "jdbc:sqlite:" + dbfile;
                conn = DriverManager.getConnection(dburl);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        conn = null;
    }

    public boolean instertKeyword(String keyword) {
        try {
            insert_keyword.setString(1, keyword);
            insert_keyword.addBatch();
            insert_keyword.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean instertIndex(int keywordID, int recordID) {
        try {
            insert_index.setInt(1, recordID);
            insert_index.setInt(2, keywordID);
            insert_index.addBatch();
            insert_index.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int instertTask(String URL, int deepth, String serach) {
        String domain = URL;
        try {
            domain = new URL(URL).getHost();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String pass = "";
        return instertTask(URL, 0, deepth, domain, pass, serach);
    }

    public int instertTask(String URL, int status, int deepth, String domain, String pass, String search) {
        try {
            insert_task.setString(1, URL);
            insert_task.setInt(2, status);
            insert_task.setInt(3, deepth);
            insert_task.setString(4, domain);
            insert_task.setString(5, pass);
            insert_task.setString(6, search);
            insert_task.addBatch();
            insert_task.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
        return taskLastID();
    }

    public Task getTaskbyID(int ID) {
        try {
            rs = stmt.executeQuery("select * from task where taskID='" + ID + "';");
            if (rs.next()) {
                Task t = new Task();
                t.taskID = rs.getInt("taskID");
                t.URL = rs.getString("URL");
                t.status = rs.getInt("status");
                t.deepth = rs.getInt("deepth");
                t.domain = rs.getString("domain");
                t.pass = rs.getString("pass");
                t.search = rs.getString("search");
                // t.crawltime=(Date)rs.getDate("crawltime");
                return t;
            } else {
                // stop crawling if reach the bottom of the list
                return null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateTask(int taskID, int status) {
        try {
            update_task.setInt(2, taskID);
            update_task.setInt(1, status);
            update_task.addBatch();
            update_task.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean crawledRecord(int recordID) {
        return updateRecord(recordID, "", "", "", 1);
    }

    public boolean updateRecord(int recordID, String title, String author, String content, int crawled) {
        try {
            update_record.setInt(1, recordID);
            update_record.setString(2, title);
            update_record.setString(3, author);
            update_record.setString(4, content);
            update_record.setInt(5, crawled);
            update_record.addBatch();
            update_record.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int instertRecord(int taskID, String URL, int deepth) {
        return instertRecord(taskID, URL, "", "", "", deepth, 0, 0);
    }

    public int instertRecord(int taskID, String URL, String title, String author, String content, int deepth, int type, int crawled) {
        try {
            insert_record.setInt(1, taskID);
            insert_record.setString(2, URL);
            insert_record.setString(3, title);
            insert_record.setString(4, author);
            insert_record.setString(5, content);
            insert_record.setInt(6, deepth);
            insert_record.setInt(7, type);
            insert_record.setInt(8, crawled);
            insert_record.addBatch();
            insert_record.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
        return recordLastID();
    }

    public Record getRecordbyID(int ID) {
        try {
            rs = stmt.executeQuery("select * from record where recordID='" + ID + "';");
            if (rs.next()) {
                Record r = new Record();
                r.recordID = rs.getInt("recordID");
                r.taskID = rs.getInt("taskID");
                r.URL = rs.getString("URL");
                r.title = rs.getString("title");
                r.author = rs.getString("author");
                r.content = rs.getString("content");
                r.deepth = rs.getInt("deepth");
                r.crawled = rs.getInt("crawled");
                r.type = rs.getInt("type");
                // r.crawltime=(Date)rs.getDate("crawltime");

                return r;
            } else {
                // stop crawling if reach the bottom of the list
                return null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public Record getRecordbyURL(String URL) {
        try {
            rs = stmt.executeQuery("select * from record where URL='" + URL + "';");
            if (rs.next()) {
                Record r = new Record();
                r.recordID = rs.getInt("recordID");
                r.taskID = rs.getInt("taskID");
                r.URL = rs.getString("URL");
                r.title = rs.getString("title");
                r.author = rs.getString("author");
                r.content = rs.getString("content");
                r.deepth = rs.getInt("deepth");
                r.crawled = rs.getInt("crawled");
                r.type = rs.getInt("type");
                // r.crawltime=(Date)rs.getDate("crawltime");

                return r;
            } else {
                // stop crawling if reach the bottom of the list
                return null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public int nextRecord(int maxdeepth) {
        try {
            rs = stmt.executeQuery("select recordID from record where crawled=0 and deepth<" + maxdeepth + ";");
            if (rs.next()) {
                return rs.getInt("recordID");
            } else {
                // stop crawling if reach the bottom of the list
                return 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public void dropTable() {
        try {

            sql = "drop table if exists task;";
            stmt.executeUpdate(sql);

            sql = "drop table if exists record;";
            stmt.executeUpdate(sql);

            sql = "drop table if exists keyword;";
            stmt.executeUpdate(sql);

            sql = "drop table if exists kwindex;";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createTable() {
        try {

            sql = "create table if not exists task (" + "taskID INTEGER PRIMARY KEY," + "URL text not null," + "status int not null," + "deepth int,"
                    + "domain text," + "pass text," + "search text," + "createtime datetime);";
            stmt.executeUpdate(sql);

            sql = "create table if not exists record (" + "recordID INTEGER PRIMARY KEY," + "taskID int not null," + "URL text not null," + "title text,"
                    + "author text," + "content tinytext," + "deepth int," + "type tinyint(1)," + "crawltime datetime," + "crawled tinyint(1) not null);";
            stmt.executeUpdate(sql);

            sql = "create table if not exists keyword (" + "keywordID INTEGER PRIMARY KEY," + "keyword text not null);";
            stmt.executeUpdate(sql);

            sql = "create table if not exists kwindex (" + "indexID INTEGER PRIMARY KEY," + "recordID int not null," + "keywordID int not null);";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int recordLastID() {
        try {
            rs = stmt.executeQuery("select max(recordID) from record;");
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                // stop crawling if reach the bottom of the list
                return 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public int taskLastID() {
        try {
            rs = stmt.executeQuery("select max(taskID) from task;");
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                // stop crawling if reach the bottom of the list
                return 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
}
