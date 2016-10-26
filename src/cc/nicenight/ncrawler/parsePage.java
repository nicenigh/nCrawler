package cc.nicenight.ncrawler;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.*;
import org.htmlparser.util.*;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class parsePage {
	private SQLiteHelper db;
	private String strurl;
	private String strcontent;
	private Record record;
	private Task task;

	public parsePage(String strurl, String strcontent) {
		super();
		this.strurl = strurl;
		this.strcontent = strcontent;
		db = new SQLiteHelper();
		record = db.getRecordbyURL(this.strurl);
		task = db.getTaskbyID(record.taskID);
		if (strurl.endsWith(".html"))
			record.type = 1;
		else
			record.type = 0;
		
		// parseContent();
		parseTitle();
		parseAuthor();
		
		if ((record.deepth < task.maxdeepth)
				&& (db.exeQrInt("select count(*) from record where taskID=" + task.taskID
						+ " and status>0;") > task.maxlinks))
			parseLinks();
		record.status = 2;
		db.updateRecord(record);
	}

	public void parseTitle() {
		try {
			Parser parser = Parser.createParser(strcontent, getEncoding());
			NodeFilter filter = new TagNameFilter("title");

			Node node = null;
			NodeList nodeList;
			nodeList = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < nodeList.size(); ++i) {
				node = nodeList.elementAt(i);
			}
			record.title = node.toPlainTextString();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseAuthor() {
		try {
			Parser parser = Parser.createParser(strcontent, getEncoding());
			NodeFilter filter = new HasAttributeFilter("id","ne_article_source");

			Node node = null;
			NodeList nodeList;
			nodeList = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < nodeList.size(); ++i) {
				node = nodeList.elementAt(i);
			}
			record.author = node.toPlainTextString();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseKeywords() {

	}

	public void parseContent() {
		try {
			Parser parser = new Parser(strcontent);

			for (NodeIterator i = parser.elements(); i.hasMoreNodes();) {
				Node node = i.nextNode();
				System.out.println("getText:" + node.getText());
				System.out.println("getPlainText:" + node.toPlainTextString());
				System.out.println("toHtml:" + node.toHtml());
				System.out.println("toHtml(true):" + node.toHtml(true));
				System.out.println("toHtml(false):" + node.toHtml(false));
				System.out.println("toString:" + node.toString());
				System.out.println("=================================================");
			}
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
	}

	public void parseLinks() {
		try {
			Parser parser = new Parser(strcontent);
			NodeFilter filter = new HasAttributeFilter("href");

			NodeList list = parser.parse(filter);

			int count = 0;

			// process every link on this page
			for (int i = 0; i < list.size(); i++) {
				if (count > task.maxlinkeachpage)
					break;

				Node node = list.elementAt(i);

				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					String nextlink = link.extractLink();
					try {
						URL nexturl = new URL(nextlink);
						// only save page from domain
						if (nexturl.getHost().equals(task.field)) {
							// do not save any page from pass
							if (nexturl.getHost().equals(task.pass))
								continue;
							// check if the link already exists in the database
							if (db.getRecordbyURL(nextlink) == null) {
								// if the link does not exist in the database,
								// insert it
								db.instertRecord(record.taskID, nextlink, record.deepth + 1);
								System.out.println("newlink:" + nextlink);
								count++;
							}
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getEncoding() {// 根据正则匹配得到页面编码
		String enC = "utf-8";
		Pattern p = Pattern.compile("(charset|Charset|CHARSET)\\s*=\\s*\"?\\s*([-\\w]*?)[^-\\w]");
		Matcher m = p.matcher(strcontent);
		if (m.find()) {
			enC = m.group(2);
		}
		return enC;
	}
}
