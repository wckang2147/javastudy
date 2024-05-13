package com.lgcns.test;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

public class JettyServer {
    public static void main(String[] args) throws Exception{
        int port = 8080; // 서버 포트 설정
            
        Server server1 = new Server();
        ServerConnector connector1 = new ServerConnector(server1);
        connector1.setPort(port);
        server1.addConnector(connector1);
        server1.setHandler(new SimpleHandler());

        server1.start();
        server1.join();
    }

    static class SimpleHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            // 클라이언트가 요청한 URL 가져오기
            String url = request.getRequestURL().toString();
            response.getWriter().println("클라이언트가 요청한 URL: " + url);
        	System.out.println(url + target);

            try {
                URI uri = new URI(url);
                String path = uri.getPath();
                String query = uri.getQuery();
                if (path != null && path.startsWith("/")) {
                    path = path.substring(1); // Remove leading "/"
                    String[] pathComponents = path.split("/");
                    for (String component : pathComponents) {
                        System.out.println("Path Component: " + component);
                    }
                }
                if (query != null) {
                    String[] queryParams = query.split("&");
                    for (String param : queryParams) {
                        String[] pair = param.split("=");
                        if (pair.length == 2) {
                            System.out.println("Query Parameter: " + pair[0] + "=" + pair[1]);
                        }
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (url.endsWith("/proxy")) {
            	System.out.println("called proxy");
            	JettyClient.sendRequest("http://localhost:8080", "GET", "", "");
            }

            // X- 로 시작하는 헤더 출력
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (headerName.toLowerCase().startsWith("x-")) {
                    String headerValue = request.getHeader(headerName);
                    response.getWriter().println(headerName + ": " + headerValue);
                }
            }
        }
    }
}



