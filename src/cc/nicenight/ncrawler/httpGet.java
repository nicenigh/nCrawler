package cc.nicenight.ncrawler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.net.URL;
import java.net.URI;

import java.io.IOException;

/**
 * Created by nicenight on 16/10/25 Site: http://www.nicenight.cc/
 */
public class httpGet {
	public final static void getByString(String strurl) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(strurl);
			//System.out.println("executing request " + httpget.getURI());

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			/*
			 * //print the content of the page
			 * System.out.println("----------------------------------------");
			 * System.out.println(responseBody);
			 * System.out.println("----------------------------------------");
			 */
			new parsePage(strurl, responseBody);
		} catch (Exception e) {
			// handle the exceptions
			System.out.println("getByString Exception: " + e.getMessage());
		} finally {
			httpclient.close();
		}
	}
}
