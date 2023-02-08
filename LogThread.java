package com.lgcns.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class LogThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			Server server = new Server();
			ServerConnector http = new ServerConnector(server);
			http.setHost("127.0.0.1");
			http.setPort(4321);
			server.addConnector(http);

			ServletHandler servletHandler = new ServletHandler();
			servletHandler.addServletWithMapping(logServlet.class, "/*");
			server.setHandler(servletHandler);

			server.start();
			System.out.println("Log Server Start with Port = " + 4321);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Already opened");
		}
	}
}
