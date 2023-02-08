package com.lgcns.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class logServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static LinkedHashMap<String, TraceJsonVo> traceMap;

	public logServlet() {
		// TODO Auto-generated constructor stub
		traceMap = new LinkedHashMap<>();

	}
//	Enumeration<String> headerNames = req.getHeaderNames();
//
//	while (headerNames.hasMoreElements()) {
//		String headerName = headerNames.nextElement();
//
//		System.out.println("[Header] Key : " + headerName + ", value : " + req.getHeader(headerName));

	private TraceJsonVo findTraceObject(TraceJsonVo traceJsonVo, String fullUrlString) {
		// TODO Auto-generated method stub
		if (traceJsonVo == null) {
			return null;
		}

		if (traceJsonVo.target.startsWith(fullUrlString))
			return traceJsonVo;

		if (traceJsonVo.services == null || traceJsonVo.services.isEmpty()) {
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

	private void printHeader(HttpServletRequest req) {

		Enumeration<String> headerNames = req.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();

			System.out.println("[4321] [Header] Key : " + headerName + ", value : " + req.getHeader(headerName));
		}

	}

	public LinkedHashMap<String, Object> formatHeader(HttpServletRequest request) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		Enumeration<String> enumber = request.getHeaderNames();

		while (enumber.hasMoreElements()) {
			String key = enumber.nextElement().toString();
			String value = request.getHeader(key);

			map.put(key, value);
		}

		return map;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("[4321] ============= GET LogServer called =====================");
		printHeader(req);
		System.out.println("[4321] ==================================");

		LinkedHashMap<String, Object> headerMap = formatHeader(req);

		String reqId = headerMap.get("x-reqId").toString();
		String reqUri = req.getRequestURI();

		if (reqUri.startsWith("/trace")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonStr = gson.toJson(traceMap.get(headerMap.get("x-reqId")));
			System.out.println("JSON = " + jsonStr);
			resp.setContentType(req.getHeader("content-type"));
			resp.setStatus(200);
			resp.getWriter().write(jsonStr);
			return;
		}
		if (reqUri.startsWith("/status")) {

			System.out.println("^^^^^^^^^============= called status");
			TraceJsonVo targeTraceJsonVo = findTraceObject(traceMap.get(reqId), headerMap.get("x-service").toString());
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonStr = gson.toJson(targeTraceJsonVo);
			System.out.println("previous JSON = " + jsonStr + req.getHeader("x-status"));

			targeTraceJsonVo.status = req.getHeader("x-status");

			jsonStr = gson.toJson(traceMap.get(reqId));
			System.out.println("changed JSON = " + jsonStr);

			System.out.println("^^^^^^^^^^[end of 4321] =========");

			resp.setContentType(req.getHeader("content-type"));
			resp.setStatus(200);
			resp.getWriter().write("");
			return;
		}

		if (traceMap.get(reqId) == null) {
			TraceJsonVo newVo = new TraceJsonVo();
			newVo.target = headerMap.get("x-target").toString();
			newVo.status = "200";
			traceMap.put(reqId, newVo);
		}

		TraceJsonVo targeTraceJsonVo = findTraceObject(traceMap.get(reqId), headerMap.get("x-target").toString());

		System.out.println(targeTraceJsonVo);
		System.out.println("---------");
		if (targeTraceJsonVo == null) {
			targeTraceJsonVo = new TraceJsonVo();
			targeTraceJsonVo.target = headerMap.get("x-target").toString();
			targeTraceJsonVo.status = "200";
		}

		TraceJsonVo serviceJsonVo = new TraceJsonVo();
		serviceJsonVo.target = headerMap.get("x-service").toString();
		serviceJsonVo.status = "200";

		if (targeTraceJsonVo.services == null)
			targeTraceJsonVo.services = new ArrayList<>();

		targeTraceJsonVo.services.add(serviceJsonVo);

		resp.setContentType(req.getHeader("content-type"));
		resp.setStatus(200);
		resp.getWriter().write("Success");

		System.out.println("[4321] =========");

		// System.out.println(traceMap.get(headerMap.get("x-reqId")));

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(traceMap.get(headerMap.get("x-reqId")));
		System.out.println("JSON = " + jsonStr);

		System.out.println("[end of 4321] =========");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
