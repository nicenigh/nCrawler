package cc.nicenight.ncrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Date;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class SQLiteHelper {

	private static Connection conn = null;
	private String dbfile;

	public SQLiteHelper(String dbfile) {
		this.dbfile = dbfile;
	}

	public SQLiteHelper() {
	}

	public Connection connect() {
		if (conn == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				String dburl = "jdbc:sqlite:" + dbfile;
				conn = DriverManager.getConnection(dburl);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return conn;
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

	public int instertKeyword(String keyword) {
		try {
			PreparedStatement insert_keyword = conn.prepareStatement("insert into keyword (keyword) values (?);");
			insert_keyword.setString(1, keyword);
			insert_keyword.addBatch();
			insert_keyword.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return getLastID("keyword");
	}

	public int instertIndx(int keywordID, int recordID) {
		try {
			PreparedStatement insert_index = conn
					.prepareStatement("insert into indx (recordID, keywordID) values (?,?);");
			insert_index.setInt(1, recordID);
			insert_index.setInt(2, keywordID);
			insert_index.addBatch();
			insert_index.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return getLastID("indx");
	}

	public int instertTask(String URL, int deepth, String serach, int maxlinks, int maxpages, int maxlinkeachpage) {
		String domain = URL;
		try {
			domain = new URL(URL).getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pass = "";
		return instertTask(URL, 0, deepth, domain, pass, serach, maxlinks, maxpages, maxlinkeachpage);
	}

	public int instertTask(String URL, int status, int deepth, String field, String pass,
			String search, int maxlinks, int maxpages, int maxlinkeachpage) {
		Task t=new Task(0,  URL,  status,  deepth,  maxlinks,  maxpages,
				 maxlinkeachpage,  field,  pass,  search, new Date()) ;
		return instertTask(t);
	}

	public int instertTask(Task t) {
		try { 
			PreparedStatement insert_task = conn
				.prepareStatement("insert into task (URL, status, deepth, field, pass, search, maxlinks, maxpages, maxlinkeachpage, createtime) values (?,?,?,?,?,?,?,?,?,datetime('now'));");
			insert_task.setString(1, t.uRL);
			insert_task.setInt(2, t.status);
			insert_task.setInt(3, t.maxdeepth);
			insert_task.setString(4, t.field);
			insert_task.setString(5, t.pass);
			insert_task.setString(6, t.search);
			insert_task.setInt(7, t.maxlinks);
			insert_task.setInt(8, t.maxpages);
			insert_task.setInt(9, t.maxlinkeachpage);
			insert_task.addBatch();
			insert_task.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return getLastID("task");
	}

	public Task getTaskbyID(int ID) {
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("select * from task where taskID=" + ID + ";");
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
			t.uRL = rs.getString("URL");
			t.status = rs.getInt("status");
			t.maxdeepth = rs.getInt("deepth");
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
	
	public int updateTask(int taskID, int status) {
		Task t=new Task(taskID,  "",  status,  0,  0,  0,
				 0,  "",  "",  "", new Date()) ;
		return updateTask(t);
	}
	
	public int updateTask(Task t) {
		try {
			PreparedStatement update_task = conn.prepareStatement("update task set status=? where taskID=?;");
			update_task.setInt(2, t.taskID);
			update_task.setInt(1, t.status);
			update_task.addBatch();
			update_task.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return t.taskID;
	}

	public int crawlingRecord(int recordID) {
		return updateRecord(recordID, "", "", "", 0, 1);
	}

	public int updateRecord(int recordID, String title, String author, String content,
			int type, int status) {
		Record r=new Record(recordID, 0,  "",  title,  author,  content,
				 0,  0,  type, new Date(), status);
		return updateRecord(r);
	}
	
	public int updateRecord(Record r) {
		try {
			PreparedStatement update_record = conn
					.prepareStatement("update record set title=?, author=?, content=?, type=?, status=?, crawltime=datetime('now') where recordID=?;");
			update_record.setInt(6, r.recordID);
			update_record.setString(1, r.title);
			update_record.setString(2, r.author);
			update_record.setString(3, r.content);
			update_record.setInt(4, r.type);
			update_record.setInt(5, r.status);
			update_record.addBatch();
			update_record.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return r.recordID;
	}

	public int instertRecord(int taskID, String URL, int deepth) {
		return instertRecord(taskID, URL, "", "", "", 0, deepth, 0, 0);
	}

	public int instertRecord(int taskID, String URL, String title, String author, String content,
			int priority, int deepth, int type, int status) {
		Record r=new Record(0, taskID,  URL,  title,  author,  content,
				 priority,  deepth,  type, new Date(), status);
		return instertRecord(r);
	}

	public int instertRecord(Record r) {
		try {
			PreparedStatement insert_record = conn
					.prepareStatement("insert into record (taskID, URL, title, author, content, priority, deepth, type, status, crawltime) values (?,?,?,?,?,?,?,?,?,datetime('now'));");
			insert_record.setInt(1, r.taskID);
			insert_record.setString(2, r.uRL);
			insert_record.setString(3, r.title);
			insert_record.setString(4, r.author);
			insert_record.setString(5, r.content);
			insert_record.setInt(6, r.priority);
			insert_record.setInt(7, r.deepth);
			insert_record.setInt(8, r.type);
			insert_record.setInt(9, r.status);
			insert_record.addBatch();
			insert_record.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return getLastID("record");
	}

	public Record getRecordbyID(int ID) {
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("select * from record where recordID=" + ID + ";");
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
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("select * from record where URL='" + URL + "';");
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
			r.uRL = rs.getString("URL");
			r.title = rs.getString("title");
			r.author = rs.getString("author");
			r.content = rs.getString("content");
			r.priority = rs.getInt("priority");
			r.deepth = rs.getInt("deepth");
			r.status = rs.getInt("status");
			r.type = rs.getInt("type");
			// r.crawltime=(Date)rs.getDate("crawltime");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
	public ResultSet exeQ(String sql)
	{
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs;
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
	
	public int exeQrInt(String sql)
	{
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				// stop crawling if reach the bottom of the list
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int nextRecord(int maxdeepth) {
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("select recordID from record where status=0 and deepth<"
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
			String sql = null;
			Statement stmt =conn.createStatement();
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
			String sql = null;
			Statement stmt =conn.createStatement();
			sql = "create table if not exists task (" + "taskID INTEGER PRIMARY KEY,"
					+ "URL text not null," + "status int not null," + "deepth int," + "field text,"
					+ "pass text," + "search text," + "maxlinks int not null,"
					+ "maxpages int not null," + "maxlinkeachpage int not null,"
					+ "createtime datetime);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists record (" + "recordID INTEGER PRIMARY KEY,"
					+ "taskID int not null," + "URL text not null," + "title text,"
					+ "author text," + "content tinytext," + "priority int not null,"
					+ "deepth int," + "type tinyint(1)," + "crawltime datetime,"
					+ "status tinyint(1) not null);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists keyword (" + "keywordID INTEGER PRIMARY KEY,"
					+ "keyword text not null);";
			stmt.executeUpdate(sql);

			sql = "create table if not exists indx (" + "indxID INTEGER PRIMARY KEY,"
					+ "recordID int not null," + "keywordID int not null);";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int getLastID(String Table) {
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("select max("+Table+"ID) from "+Table+";");
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
