package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

public class ProxyServlet extends HttpServlet {
	public static HashMap<String, TraceJsonVo> traceMap;

	public ProxyServlet() {
		// TODO Auto-generated constructor stub
		traceMap = new HashMap<>();
	}

	public HashMap<String, Object> formatMapRequest(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Enumeration<String> enumber = request.getParameterNames();

		while (enumber.hasMoreElements()) {
			String key = enumber.nextElement().toString();
			String value = request.getParameter(key);

			map.put(key, value);
		}

		return map;
	}

	public HashMap<String, Object> formatHeader(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Enumeration<String> enumber = request.getHeaderNames();

		while (enumber.hasMoreElements()) {
			String key = enumber.nextElement().toString();
			String value = request.getHeader(key);

			map.put(key, value);
		}

		return map;
	}

	public ContentResponse sendHttpMessage2(String url, HttpMethod method, HashMap<String, Object> headers,
			String bodyStr) throws Exception {

		HttpClient httpClient = new HttpClient();
		httpClient.start();

		Request request = httpClient.newRequest(url).method(method); // .timeout(5, TimeUnit.SECONDS);

		if (method.equals(HttpMethod.POST))
			request.content(new StringContentProvider(bodyStr, "utf-8"));

		// set header from parameter
		for (String headerKey : headers.keySet()) {
			request.header(headerKey, headers.get(headerKey).toString());
		}
		// printResHeader(request.getHeaders());

		ContentResponse contentRes = request.send();
		httpClient.stop();
		return contentRes;
	}

	public ContentResponse sendHttpMessage(String url, HttpMethod method, HttpServletRequest req, String bodyStr)
			throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();

		Request request = httpClient.newRequest(url).method(method).timeout(5, TimeUnit.SECONDS);
		if (method.equals(HttpMethod.POST))
			request.content(new StringContentProvider(bodyStr, "utf-8"));

		// Copy header from previous message.
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			request.header(headerName, req.getHeader(headerName));

		}

		printResHeader(request.getHeaders());

		System.out.println("Call http server = " + url);
		ContentResponse contentRes = request.send();
		System.out.println(contentRes.getContentAsString());
		httpClient.stop();
		return contentRes;
	}
//
//	public static ContentResponse sendPostJson(String url, String bodyStr) throws Exception {
//
//		// String jsonStr = "{\"key\": \"value\"}";
//
//		HttpClient httpClient = new HttpClient();
//		httpClient.start();
//
//		// .header("content-type, "value1")
//		Request req = httpClient.newRequest(url).method(HttpMethod.POST)
//				.content(new StringContentProvider(bodyStr, "utf-8")).timeout(5, TimeUnit.SECONDS);
//
//		ContentResponse response = req.send();
//
//		System.out.println(response.getContentAsString());
//
//		// httpClient.stop();
//		return response;
//	}
//	public ContentResponse sendHttpGet(String url) throws Exception {
//		HttpClient httpClient = new HttpClient();
//		httpClient.start();
//		Request request = httpClient.newRequest(url).method(HttpMethod.GET);
//		ContentResponse contentRes = request.send();
//		System.out.println(contentRes.getContentAsString());
//		return contentRes;
//	}

	public static String findUrl(String reqUri) {
		String urlString = null;
		for (int i = 0; i < RunManager.jsonProxy.routes.size(); i++) {
			String pathPrefixString;
			pathPrefixString = RunManager.jsonProxy.routes.get(i).get("pathPrefix").toString();
			// System.out.println("pathPrefixString = " + pathPrefixString);

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
	 * GET 占쎌뒄筌ｏ옙 筌ｌ꼶�봺
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String reqUri = req.getRequestURI();
		String reqQueString = req.getQueryString();
//		String reqFullUrl = req.getRequestURL().toString();
		String calledFullUrl = req.getScheme() + "://" + req.getLocalAddr() + ":" + req.getLocalPort() + reqUri;
		String myIpPortString = req.getScheme() + "://" + req.getLocalAddr() + ":" + req.getLocalPort();
		System.out.println("Request URI : " + reqUri);
		System.out.println("called Full Url : " + calledFullUrl);
		System.out.println("Request remote Port : " + req.getRemotePort());
		System.out.println("Request Port : " + req.getLocalPort());
		System.out.println("Query String : " + req.getQueryString());

		ContentResponse contentRes;

		String urlString = findUrl(reqUri);
		if (urlString == null)
			System.out.println("==== Cannot found url. url string is null");
		String fullUrlString;
		fullUrlString = urlString + reqUri;

		String reqId = req.getHeader("x-requestId");
		String depthId = req.getHeader("x-depthId");
		String service = req.getHeader("x-service");
		String target = req.getHeader("x-target");
		String status = req.getHeader("x-status");

		HashMap<String, Object> headerMap = formatHeader(req);

		if (depthId == null)
			headerMap.put("x-depthId", "1");

		if (status == null)
			headerMap.put("x-status", "200");

		// send to log store
		HashMap<String, Object> logHeaderMap = formatHeader(req);
		if (reqId != null)
			logHeaderMap.put("x-reqId", reqId);

		if (reqUri.startsWith("/trace")) {
			String logUrlString = new String("http://127.0.0.1:4321/trace");

			String reqIdString;
			reqIdString = reqUri.substring(reqUri.lastIndexOf("/") + 1);
			System.out.println("@@@@@@@@@@ ID = " + reqIdString);
			logHeaderMap.put("x-reqId", reqIdString);
			try {
				contentRes = sendHttpMessage2(logUrlString, HttpMethod.GET, logHeaderMap, "");
				HttpFields resHeaders = contentRes.getHeaders();
				resHeaders.forEach((httpField) -> {
					if (httpField.getName().startsWith("x-")) {
						res.setHeader(httpField.getName(), httpField.getValue());
					}
				});
//					res.setContentType(req.getHeader("content-type").toString());
				res.setStatus(contentRes.getStatus());

				res.getWriter().write(contentRes.getContentAsString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;

		}

		System.out.println("target = " + target + " & myIpPort = " + myIpPortString);
		if (target != null && target.startsWith(myIpPortString)) {
			System.out.println("**** called by service");

			logHeaderMap.put("x-target", service);
			logHeaderMap.put("x-service", calledFullUrl);

			try {
				String logUrlString = new String("http://127.0.0.1:4321/log");
				sendHttpMessage2(logUrlString, HttpMethod.GET, logHeaderMap, "");

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (calledFullUrl != null) {
			logHeaderMap.put("x-target", calledFullUrl);
			headerMap.put("x-target", calledFullUrl);
		}
		if (fullUrlString != null) {
			headerMap.put("x-service", fullUrlString);
			logHeaderMap.put("x-service", fullUrlString);
		}
		if (depthId != null)
			logHeaderMap.put("x-depthId", depthId);

		try {
			String logUrlString = new String("http://127.0.0.1:4321/log");
			sendHttpMessage2(logUrlString, HttpMethod.GET, logHeaderMap, "");

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpFields resHeaders;
		if (urlString != null) {
			try {
				if (reqQueString != null)
					fullUrlString += "?" + reqQueString;
				HashMap<String, Object> statusMap = new HashMap<>();

				System.out.println("!!!!!!!! call url = " + fullUrlString);

				ContentResponse contentRes1 = sendHttpMessage2(fullUrlString, HttpMethod.GET, headerMap, "");

				int rescode = contentRes1.getStatus();
				// if (rescode != 200) {
				res.setHeader("x-status", Integer.toString(rescode));
				statusMap.put("x-status", Integer.toString(rescode));
				// }
				resHeaders = contentRes1.getHeaders();

				resHeaders.forEach((httpField) -> {
					if (httpField.getName().startsWith("x-")) {
						res.setHeader(httpField.getName(), httpField.getValue());
					}
				});
				System.out.println("Res code = " + contentRes1.getStatus());

				res.setContentType(resHeaders.get("content-type").toString());
				res.setStatus(rescode);

				System.out.println("rescode" + rescode);
				System.out.println("Ful URL String = " + fullUrlString);
				try {
					String logUrlString = new String("http://127.0.0.1:4321/status");

					statusMap.put("x-reqId", reqId);
					statusMap.put("x-service", fullUrlString);

					System.out.println("send http to + " + logUrlString + " with " + fullUrlString + " " + reqId + " "
							+ statusMap.get("x-status"));
					sendHttpMessage2(logUrlString, HttpMethod.GET, statusMap, "");

				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				try {
					String logUrlString = new String("http://127.0.0.1:4321/status");

					statusMap.put("x-reqId", reqId);
					statusMap.put("x-service", calledFullUrl);

					System.out.println("send http to + " + logUrlString + " with " + fullUrlString + " " + reqId + " "
							+ statusMap.get("x-status"));
					sendHttpMessage2(logUrlString, HttpMethod.GET, statusMap, "");

				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				res.getWriter().write(contentRes1.getContentAsString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private TraceJsonVo findTraceObject(TraceJsonVo traceJsonVo, String fullUrlString) {
		// TODO Auto-generated method stub
		if (traceJsonVo.services.isEmpty()) {
			System.out.println("target = " + traceJsonVo.target + " <--> fullUrlString = " + fullUrlString);
			if (traceJsonVo.target.startsWith(fullUrlString))
				return traceJsonVo;
			else
				return null;
		} else {
			for (TraceJsonVo service : traceJsonVo.services) {
				TraceJsonVo temp = findTraceObject(service, fullUrlString);
				if (temp != null)
					return temp;
			}
		}
		return null;
	}

//
//	private void simulateBlock(String reqUri) {
//		// regex test online => https://regex101.com/
//		Pattern p = Pattern.compile("^/(api)/(block)/([0-9]+)"); // api/block/4
//		Matcher matcher = p.matcher(reqUri);
//
//		if (matcher.matches()) {
////            System.out.println(matcher.group(1));    // api
////            System.out.println(matcher.group(2));    // block
////            System.out.println(matcher.group(3));    // 4
//
//			int sleepSec = Integer.parseInt(matcher.group(3));
//
//			try {
//				Thread.sleep(sleepSec * 1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static ContentResponse sendPostJson(String url, String bodyStr) throws Exception {
//
//		// String jsonStr = "{\"key\": \"value\"}";
//
//		HttpClient httpClient = new HttpClient();
//		httpClient.start();
//		
//		// .header("content-type, "value1")
//		Request req = httpClient.newRequest(url).method(HttpMethod.POST)
//				.content(new StringContentProvider(bodyStr, "utf-8")).timeout(5, TimeUnit.SECONDS);
//
//		ContentResponse response = req.send();
//
//		System.out.println(response.getContentAsString());
//
//		// httpClient.stop();
//		return response;
//	}

	/**
	 * POST 占쎌뒄筌ｏ옙 筌ｌ꼶�봺
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

				contentRes = sendHttpMessage(fullUrlString, HttpMethod.GET, req, sb.toString());

				HttpFields resHeaders = contentRes.getHeaders();
				resHeaders.forEach((httpField) -> {
					if (httpField.getName().startsWith("x-")) {
						res.setHeader(httpField.getName(), httpField.getValue());
					}
				});
				res.setContentType(resHeaders.get("content-type").toString());
				res.setStatus(contentRes.getStatus());

				res.getWriter().write(contentRes.getContentAsString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

//	private void printHeader(HttpServletRequest req) {
//
//		Enumeration<String> headerNames = req.getHeaderNames();
//
//		while (headerNames.hasMoreElements()) {
//			String headerName = headerNames.nextElement();
//
//			System.out.println("[Header] Key : " + headerName + ", value : " + req.getHeader(headerName));
//		}
//
//	}
}