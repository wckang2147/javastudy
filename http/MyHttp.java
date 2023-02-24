package com.lgcns.test;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class MyHttp extends Thread {

	static int port = 8080;

	public void start() {

		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(port);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyHttpServlet.class, "/*");
		server.setHandler(servletHandler);

		try {
			server.start();
			System.out.println("Server Start with Port = " + port);
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyHttp server = new MyHttp();
		server.start();

		try {
//			sendHttpSimple("http://127.0.0.1:8080/RECEIVE/123.jsp?q1=123&q2=456", HttpMethod.GET, null);
			ContentResponse res = sendHttpSimple("http://127.0.0.1:8080/CREATE", HttpMethod.POST, "{\"QueueSize\":3}");

			System.out.println("Response status : " + res.getStatus());

//			HttpFields resHeaders = res.getHeaders();
//
//			resHeaders.forEach((httpField) -> {
//				System.out.println(
//						"[Response Header] key : " + httpField.getName() + ", value : " + httpField.getValue());
//			});
//
//			System.out.println(resHeaders.get("content-type"));
//			System.out.println(resHeaders.get("cache-control"));
//			System.out.println(resHeaders.get("date"));

//			if (res.getStatus() == 200) {
//				System.out.println(res.getContentAsString());
//			} else if (res.getStatus() == 500) {
//				System.out.println("500 error");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ContentResponse sendHttpSimple(String url, HttpMethod method, String bodyStr) throws Exception {

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		Request request = httpClient.newRequest(url).method(method); // .timeout(5, TimeUnit.SECONDS);
//      .header("X-Header1", "value1")
//      .header("X-Header2", "value2")
//      .header("X-Header3", "value3");
//      .timeout(5, TimeUnit.SECONDS);

		if (method.equals(HttpMethod.POST)) {
			request.header(HttpHeader.CONTENT_TYPE, "application/json");
			request.content(new StringContentProvider(bodyStr, "utf-8"));
//			request.param("Param1", "Test");
		}

		ContentResponse contentRes = request.send();

		httpClient.stop();
		return contentRes;
	}

}
