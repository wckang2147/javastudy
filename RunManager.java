package com.lgcns.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class RunManager {
	static JsonVo jsonProxy;

	public static void loadJsonFile(String fileName) {
		WckangJson loader = new WckangJson();
		jsonProxy = loader.loadData(fileName);

//		System.out.println("Json result = " + json.toString());
//		 loader.writeJsonFile(json, fileName);
//		System.out.println(json.routes.get(1).get("pathPrefix"));

	}

	public void start() throws Exception {

		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(jsonProxy.port);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(WckangServlet.class, "/*");
		server.setHandler(servletHandler);

		System.out.println("Server Start with Port = " + jsonProxy.port);
		server.start();
		server.join();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		loadJsonFile(args[0]);

		RunManager server = new RunManager();
		server.start();
	}

}
