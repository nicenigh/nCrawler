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
 * Created by nicenight on 16/10/25 025. Site: http://www.nicenight.cc/
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
					.prepareStatement("insert into task (URL, status, deepth, field, pass, search, maxlinks, maxpages, maxlinkeachpage, createtime) values (?,?,?,?,?,?,?,?,?,datetime('now'));");
			insert_record = conn
					.prepareStatement("insert into record (taskID, URL, title, author, content, priority, deepth, type, crawled, crawltime) values (?,?,?,?,?,?,?,?,?,datetime('now'));");
			insert_keyword = conn
					.prepareStatement("insert into keyword (keyword) values (?);");
			insert_index = conn
					.prepareStatement("insert into kwindex (recordID, keywordID) values (?,?);");
			update_task = conn
					.prepareStatement("update task set status=? where taskID=?;");
			update_record = conn
					.prepareStatement("update record set title=?, author=?, content=?, crawled=?, crawltime=datetime('now') where recordID=?;");
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
		return instertTask(URL, 0, deepth, domain, pass, serach, 100, 10, 10);
	}

	public int instertTask(String URL, int status, int deepth, String field,
			String pass, String search, int maxlinks, int maxpages,
			int maxlinkeachpage) {
		try {
			insert_task.setString(1, URL);
			insert_task.setInt(2, status);
			insert_task.setInt(3, deepth);
			insert_task.setString(4, field);
			insert_task.setString(5, pass);
			insert_task.setString(6, search);
			insert_task.setInt(7, maxlinks);
			insert_task.setInt(8, maxpages);
			insert_task.setInt(9, maxlinkeachpage);
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
			rs = stmt.executeQuery("select * from task where taskID='" + ID
					+ "';");
			if (rs.next()) {
				return getTask(rs);
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

	private Task getTask(ResultSet rs) {
		Task t = new Task();
		try {
			t.taskID = rs.getInt("taskID");
			t.URL = rs.getString("URL");
			t.status = rs.getInt("status");
			t.deepth = rs.getInt("deepth");
			t.field = rs.getString("field");
			t.pass = rs.getString("pass");
			t.maxlinks = rs.getInt("maxlinks");
			t.maxpages = rs.getInt("maxpages");
			t.maxlinkeachpage = rs.getInt("maxlinkeachpage");
			t.search = rs.getString("search");
			// t.crawltime=(Date)rs.getDate("crawltime");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
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

	public boolean updateRecord(int recordID, String title, String author,
			String content, int crawled) {
		try {
			update_record.setInt(5, recordID);
			update_record.setString(1, title);
			update_record.setString(2, author);
			update_record.setString(3, content);
			update_record.setInt(4, crawled);
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
		return instertRecord(taskID, URL, "", "", "", 0, deepth, 0, 0);
	}

	public int instertRecord(int taskID, String URL, String title,
			String author, String content, int priority, int deepth, int type,
			int crawled) {
		try {
			insert_record.setInt(1, taskID);
			insert_record.setString(2, URL);
			insert_record.setString(3, title);
			insert_record.setString(4, author);
			insert_record.setString(5, content);
			insert_record.setInt(6, priority);
			insert_record.setInt(7, deepth);
			insert_record.setInt(8, type);
			insert_record.setInt(9, crawled);
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
			rs = stmt.executeQuery("select * from record where recordID='" + ID
					+ "';");
			if (rs.next()) {
				return getRecord(rs);
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
			rs = stmt.executeQuery("select * from record where URL='" + URL
					+ "';");
			if (rs.next()) {
				return getRecord(rs);
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

	public Record getRecord(ResultSet rs) {
		Record r = new Record();
		try {
			r.recordID = rs.getInt("recordID");
			r.taskID = rs.getInt("taskID");
			r.URL = rs.getString("URL");
			r.title = rs.getString("title");
			r.author = rs.getString("author");
			r.content = rs.getString("content");
			r.priority = rs.getInt("priority");
			r.deepth = rs.getInt("deepth");
			r.crawled = rs.getInt("crawled");
			r.type = rs.getInt("type");
			// r.crawltime=(Date)rs.getDate("crawltime");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	public int nextRecord(int maxdeepth) {
		try {
			rs = stmt
					.executeQuery("select recordID from record where crawled=0 and deepth<"
							+ maxdeepth + ";");
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

			sql = "create table if not exists task ("
					+ "taskID INTEGER PRIMARY KEY," + "URL text not null,"
					+ "status int not null," + "deepth int," + "field text,"
					+ "pass text," + "search text," + "maxlinks int not null,"
					+ "maxpages int not null,"
					+ "maxlinkeachpage int not null," + "createtime datetime);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists record ("
					+ "recordID INTEGER PRIMARY KEY," + "taskID int not null,"
					+ "URL text not null," + "title text," + "author text,"
					+ "content tinytext," + "priority int not null,"
					+ "deepth int," + "type tinyint(1),"
					+ "crawltime datetime," + "crawled tinyint(1) not null);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists keyword ("
					+ "keywordID INTEGER PRIMARY KEY,"
					+ "keyword text not null);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists kwindex ("
					+ "indexID INTEGER PRIMARY KEY," + "recordID int not null,"
					+ "keywordID int not null);";
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
