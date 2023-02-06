package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * GET
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String reqUri = req.getRequestURI();

		System.out.println("Request URI : " + reqUri);

		// /api/block/# �슂泥��뿉 ���븯�뿬 二쇱뼱吏� �떆媛� sleep 泥섎━
		if (reqUri.startsWith("/api/block")) {
			this.simulateBlock(reqUri);
		}

		res.setStatus(200);
		res.setHeader("res-custom-header", "Set response header");
		res.getWriter().write("Request URI : " + reqUri);
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

	/**
	 * POST
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String reqUri = req.getRequestURI();

		System.out.println("Request URI : " + reqUri);

		this.printHeader(req);

		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

		String line = null;
		StringBuffer sb = new StringBuffer();

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		System.out.println("[Request body]");
		System.out.println(sb.toString());

		res.setStatus(200);
		res.setHeader("Res-custom-header", "Set response header");
		res.getWriter().write("Request URI : " + reqUri);
	}

	/**
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
