package com.lgcns.test;

import java.net.Socket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class RunManager extends Thread {
	static JsonVo jsonProxy;

	public static void loadJsonFile(String fileName) {
		WckangJson loader = new WckangJson();
		jsonProxy = loader.loadData(fileName);

//		System.out.println("Json result = " + json.toString());
//		loader.writeJsonFile(json, fileName);
//		System.out.println(json.routes.get(1).get("pathPrefix"));

	}

	public static boolean availablePort(String host, int port) {
		boolean result = false;

		try {
			(new Socket(host, port)).close();

			result = true;
		} catch (Exception e) {

		}
		return result;
	}

	public void start() {

		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(jsonProxy.port);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(ProxyServlet.class, "/*");
		server.setHandler(servletHandler);

		try {
			server.start();
			System.out.println("Server Start with Port = " + jsonProxy.port);
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startLogServer(int port) {
		try {

			Server server = new Server();
			ServerConnector http = new ServerConnector(server);
			http.setHost("127.0.0.1");
			http.setPort(port);
			server.addConnector(http);

			ServletHandler servletHandler = new ServletHandler();
			servletHandler.addServletWithMapping(logServlet.class, "/*");
			server.setHandler(servletHandler);

			server.start();
			System.out.println("Log Server Start with Port = " + port);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Already opened");
		}
	}

	static int logPort = 4321;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		startLogServer(logPort);
//		boolean portCheck = availablePort("127.0.0.1", logPort);
//		if (portCheck)
//			startLogServer(logPort);
//		else {
//			System.out.println("Already opened");
//		}

		Thread thread = new LogThread();
		thread.start();
		Thread.sleep(100);
		try {
			(new Socket("127.0.0.1", 4321)).close();
		} catch (Exception e) {

		}
		loadJsonFile(args[0]);

		RunManager server = new RunManager();
		server.start();

	}

}
