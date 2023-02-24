package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

 class QueueInfoVO {
	int QueueSize;
//	int ProcessTimeout;
//	int MaxFailCount;
//	int WaitTime;
}
public class MyHttpServlet extends HttpServlet {

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
		
		// response message
		PrintWriter out = resp.getWriter();
		out.print("Return...");
		out.flush(); 
		out.close();
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		POST http://127.0.0.1:8080/CREATE/<QName>

		StringBuffer fullURL = req.getRequestURL(); 	// http://127.0.0.1:8080/RECEIVE/<Queue Name>
		String uriString = req.getRequestURI(); // /RECEIVE/<QName>

		String[] uri = uriString.split("/");    
		String command = uri[1];				// RECEIVE
		String qName = uri[uri.length - 1];		// <QName>
		
		System.out.println("fullURL = " + fullURL);
		System.out.println("uriString = " + uriString );
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
		} catch (Exception e) {}
		
		System.out.println("body = " + jb);

		// simply parse json from body
		Gson gson = new Gson();
//		Gson gson = new GsonBuilder()				
//				  .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL).create();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = gson.fromJson(jb.toString(), Map.class);
		System.out.println(map.get("QueueSize").toString() );

		int size ;// = (int) Math.round( Double.parseDouble (  map.get("QueueSize").toString() ) );
		Double d = Double.parseDouble (  map.get("QueueSize").toString() );
		size = d.intValue();
		
		System.out.println("QueueSize = " + size);

		// parse json and store to class from body using VO
		QueueInfoVO queueInfo = gson.fromJson(jb.toString(), QueueInfoVO.class);
		System.out.println("QueueSize w/VO = " + queueInfo.QueueSize);
		
		
		// response message
//		resp.setContentType("text/html;charset=utf-8");
		//resp.setStatus(500);
		PrintWriter out = resp.getWriter();
		out.print("{\"Result\":\"OK\"}");
		out.flush(); 
		out.close();
	}
}
