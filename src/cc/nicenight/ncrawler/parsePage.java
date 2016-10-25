package cc.nicenight.ncrawler;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import java.net.URL;

import java.net.MalformedURLException;

/**
 * Created by nicenight on 16/10/25 025.
 * Site:  http://www.nicenight.cc/
 */
public class parsePage {
    public static void parseFromString(String content, Record record, SQLiteHelper db) {
        Parser parser;
        try {
            parser = new Parser(content);
            HasAttributeFilter filter = new HasAttributeFilter("href");

            Task task = db.getTaskbyID(record.taskID);
            NodeList list = parser.parse(filter);
            int count = list.size();

            // process every link on this page
            for (int i = 0; i < count; i++) {
                Node node = list.elementAt(i);

                if (node instanceof LinkTag) {
                    LinkTag link = (LinkTag) node;
                    String nextlink = link.extractLink();
                    try {
                        URL nexturl=new URL(nextlink);
                        // only save page from domain
                        if (nexturl.getHost().equals(task.field)) {

                            // do not save any page from pass
                            if (nexturl.getHost().equals(task.pass)) {
                                continue;
                            }

                            // check if the link already exists in the database
                            if (db.getRecordbyURL(nextlink) != null) {
                                System.out.println("pass:" + nextlink);
                            } else {
                                // if the link does not exist in the database,
                                // insert it
                                db.instertRecord(record.taskID, nextlink, record.deepth + 1);
                                System.out.println("new:" + nextlink);

								/*
                                // use substring for better comparison
                                // performance
                                // nextlink = nextlink.substring(mainurl.length());
                                // System.out.println(nextlink);
								 * if (nextlink.startsWith("tag/")) { tag =
								 * nextlink.substring(4, nextlink.length() - 1); //
								 * decode in UTF-8 for Chinese characters tag =
								 * URLDecoder.decode(tag, "UTF-8"); db =
								 * "INSERT INTO tags (tagname) VALUES ('" + tag +
								 * "')"; stmt.executeQuery(db); // if the links are
								 * different from each // other, the tags must be
								 * different // so there is no need to check if the
								 * tag // already exists
								 */
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
}
