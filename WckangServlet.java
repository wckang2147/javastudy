package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;

public class WckangServlet extends HttpServlet {

	public ContentResponse sendHttpGet(String url) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		Request request = httpClient.newRequest(url).method(HttpMethod.GET);
		ContentResponse contentRes = request.send();
		System.out.println(contentRes.getContentAsString());
		return contentRes;
	}

	public static String findUrl(String reqUri) {
		String urlString = null;
		for (int i = 0; i < RunManager.jsonProxy.routes.size(); i++) {
			String pathPrefixString;
			pathPrefixString = RunManager.jsonProxy.routes.get(i).get("pathPrefix").toString();
			System.out.println("pathPrefixString = " + pathPrefixString);

			if (reqUri.startsWith(pathPrefixString)) {
				urlString = RunManager.jsonProxy.routes.get(i).get("url").toString();
				System.out.println("found urlString = " + urlString);
				break;
			}
		}
		return urlString;
	}

	public void printResHeader(HttpFields res) {
		res.forEach((httpField) -> {
			System.out.println("[Response Header] key : " + httpField.getName() + ", value : " + httpField.getValue());
		});

	}

	private static final long serialVersionUID = 1L;

	/**
	 * GET �슂泥� 泥섎━
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String reqUri = req.getRequestURI();
		String reqQueString = req.getQueryString();
		System.out.println("Request URI : " + reqUri);
		System.out.println("Query String : " + req.getQueryString());
		String urlString;
		ContentResponse contentRes;

		urlString = findUrl(reqUri);
		String fullUrlString;
		fullUrlString = urlString + reqUri;

		if (urlString != null) {
			try {
				if (reqQueString != null)
					fullUrlString += "?" + reqQueString;

				contentRes = sendHttpGet(fullUrlString);
				HttpFields resHeaders = contentRes.getHeaders();
//				printResHeader (resHeaders);

				resHeaders.forEach((httpField) -> {
					if (httpField.getName().startsWith("x-")) {
						res.setHeader(httpField.getName(), httpField.getValue());
					}
				});
				res.setContentType(resHeaders.get("content-type").toString());
				res.setStatus(200);

				res.getWriter().write(contentRes.getContentAsString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void simulateBlock(String reqUri) {
		// regex test online => https://regex101.com/
		Pattern p = Pattern.compile("^/(api)/(block)/([0-9]+)"); // api/block/4
		Matcher matcher = p.matcher(reqUri);

		if (matcher.matches()) {
//            System.out.println(matcher.group(1));    // api
//            System.out.println(matcher.group(2));    // block
//            System.out.println(matcher.group(3));    // 4

			int sleepSec = Integer.parseInt(matcher.group(3));

			try {
				Thread.sleep(sleepSec * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static ContentResponse sendPostJson(String url, String bodyStr) throws Exception {

		// String jsonStr = "{\"key\": \"value\"}";

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		// .header("content-type, "value1")
		Request req = httpClient.newRequest(url).method(HttpMethod.POST)
				.content(new StringContentProvider(bodyStr, "utf-8")).timeout(5, TimeUnit.SECONDS);

		ContentResponse response = req.send();

		System.out.println(response.getContentAsString());

		// httpClient.stop();
		return response;
	}

	/**
	 * POST �슂泥� 泥섎━
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String reqUri = req.getRequestURI();
		String reqQueString = req.getQueryString();

		System.out.println("Request URI : " + reqUri);
		System.out.println("Query String : " + req.getQueryString());
		String urlString;
		ContentResponse contentRes;

//		this.printHeader(req);

		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

		String line = null;
		StringBuffer sb = new StringBuffer();

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		System.out.println("[Request body]");
		System.out.println(sb.toString());

		urlString = findUrl(reqUri);
		if (urlString != null) {
			try {
				String fullUrlString;
				fullUrlString = urlString + reqUri;
				if (reqQueString != null)
					fullUrlString += "?" + reqQueString;
				contentRes = sendPostJson(fullUrlString, sb.toString());
				HttpFields resHeaders = contentRes.getHeaders();
//				printResHeader (resHeaders);
				resHeaders.forEach((httpField) -> {
					if (httpField.getName().startsWith("x-")) {
						res.setHeader(httpField.getName(), httpField.getValue());
					}
				});
//				res.setHeader("Res-custom-header", "Set response header");
				res.setContentType(resHeaders.get("content-type").toString());
				res.setStatus(200);

				res.getWriter().write(contentRes.getContentAsString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * �슂泥� �뿤�뜑 泥섎━
	 *
	 * @param req
	 */
	private void printHeader(HttpServletRequest req) {

		Enumeration<String> headerNames = req.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();

			System.out.println("[Header] Key : " + headerName + ", value : " + req.getHeader(headerName));
		}

	}
}