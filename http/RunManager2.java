package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.gson.Gson;

class RoutingRuleVO {
	public transient int seq;
	//@SerializedName("testId") 
	public int port;
	public List <Map<String, String>> routes;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "RoutingRule Class [port=" + port + ", name=" + routes + "]";
	}
}

public class RunManager {
	static RoutingRuleVO routing;
	private static void readJsonFile(String name) {
		try {
			Reader reader = new FileReader(name);
			Gson gson = new Gson();
			routing = gson.fromJson(reader, RoutingRuleVO.class); 
			System.out.println(routing);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws Throwable {

		readJsonFile(args[0]);
		
		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(routing.port);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyHttpServlet.class, "/*");
		server.setHandler(servletHandler);

		try {
			server.start();
			System.out.println("Server Start with Port = " + routing.port);
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
