package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.ToNumberPolicy;

class QueueInfoVO {
	int QueueSize;
//	int ProcessTimeout;
//	int MaxFailCount;
//	int WaitTime;
}

public class MyHttpServlet extends HttpServlet {

	public static ContentResponse sendHttpSimple(String url, HttpMethod method, HashMap<String, String> headers,
			String bodyStr) throws Exception {

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		Request request = httpClient.newRequest(url).method(method); // .timeout(5, TimeUnit.SECONDS);
//      .header("X-Header1", "value1")
//      .header("X-Header2", "value2")
//      .header("X-Header3", "value3");
//      .timeout(5, TimeUnit.SECONDS);

		for (String key : headers.keySet()) {
			request.header(key, headers.get(key));
		}

		if (method.equals(HttpMethod.POST)) {
//			request.header(HttpHeader.CONTENT_TYPE, "application/json");
			request.content(new StringContentProvider(bodyStr, "utf-8"));
		}

		ContentResponse contentRes = request.send();

		httpClient.stop();
		return contentRes;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		GET http://127.0.0.1:8080/RECEIVE/<QName>?q1=123&q2=456
		
		StringBuffer fullURL = req.getRequestURL(); 	// http://127.0.0.1:8080/RECEIVE/<Queue Name>
		String uriString = req.getRequestURI(); // /RECEIVE/<QName>

		String[] uri = uriString.split("/");    
		String command = uri[1];				// RECEIVE
		String qName = uri[uri.length - 1];		// <QName>
		
		System.out.println("fullURL = " + fullURL);
		System.out.println("uriString = " + uriString );
		System.out.println("command = " + command + ", qName = " + qName);
		System.out.println("Query String : " + req.getQueryString()); // q1=123&q2=456
		System.out.println("Query String : " + req.getParameter("q1")); // 123
		System.out.println("Query String : " + req.getParameter("q2")); // 456

		
		String serviceURL = findService("/" + command);
		System.out.println(serviceURL);
		
		processService(req, resp, uriString, "", serviceURL, HttpMethod.GET);
		
	}

	private String findService(String command) {
		for (Map<String, String> prefix : RunManager.routing.routes) {
			if (prefix.get("pathPrefix").equals(command))
				return prefix.get("url").toString();
		}
		return "";

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		POST http://127.0.0.1:8080/CREATE/<QName>

		StringBuffer fullURL = req.getRequestURL(); // http://127.0.0.1:8080/RECEIVE/<Queue Name>
		String uriString = req.getRequestURI(); // /RECEIVE/<QName>

		String[] uri = uriString.split("/");
		String command = uri[1]; // RECEIVE
		String qName = uri[uri.length - 1]; // <QName>

		System.out.println("fullURL = " + fullURL);
		System.out.println("uriString = " + uriString);
		System.out.println("command = " + command + ", qName = " + qName);
		System.out.println("Query String : " + req.getQueryString()); // q1=123&q2=456
		System.out.println("Query String : " + req.getParameter("Param1")); // 123

		// copy body
		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
		}

		String serviceURL = findService("/" + command);
		System.out.println(serviceURL);
		
		processService(req, resp, uriString, jb.toString(), serviceURL, HttpMethod.POST);

	}

	private void processService(HttpServletRequest req, HttpServletResponse resp, String uriString, String string,
			String serviceURL, HttpMethod method) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", req.getHeader("Content-Type"));
		ContentResponse result =null;
		try {
			String url;
			if (req.getQueryString() != null)
				url = serviceURL + uriString + "?" + req.getQueryString();
			else
				url = serviceURL + uriString ;
			result = sendHttpSimple(url, method, headers, string.toString());

			// response message
			HttpFields resHeaders = result.getHeaders();
		    resHeaders.forEach((httpField) -> {
		    	resp.setHeader( httpField.getName() , httpField.getValue());
		    });
			resp.setStatus( result.getStatus() );
			PrintWriter out = resp.getWriter();
			
			out.print(result.getContentAsString());
			out.flush(); 
			out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
