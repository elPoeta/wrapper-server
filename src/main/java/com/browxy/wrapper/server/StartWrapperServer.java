package com.browxy.wrapper.server;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.server.config.Config;
import com.browxy.wrapper.server.config.PropertiesReader;
import com.browxy.wrapper.server.servlets.FileUploadServlet;

public class StartWrapperServer {
	private static final Logger logger = LoggerFactory.getLogger(StartWrapperServer.class);

	public static void main(String[] args) throws Exception {
		ValidationSystemProps validationSystemProps = validateSystemProperties();
		if (!validationSystemProps.isValid()) {
			throw new RuntimeException(validationSystemProps.getMessage());
		}
        Config config = PropertiesReader.read();
        if(config == null) {
        	throw new RuntimeException("Server config not loaded...");
        }
		String containerBasePath = System.getProperty("containerBasePath");
        
		Thread jettyThread = new Thread(() -> startJettyServer(config, containerBasePath));
		jettyThread.start();

		int webSocketPort = Integer.parseInt(System.getProperty("PORT"));
		Thread webSocketThread = new Thread(() -> startWebSocketServer(webSocketPort));
		webSocketThread.start();
	}

	private static void startJettyServer(Config config, String containerBasePath) {
		try {
			Server jettyServer = new Server(config.getPort());

			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setResourceBase(containerBasePath + config.getStaticDir());
			resourceHandler.setWelcomeFiles(new String[] { config.getStaticFile() });

	        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        servletContextHandler.addServlet(new ServletHolder(new FileUploadServlet(containerBasePath)), "/upload");
		
			WebSocketHandler wsHandler = new WebSocketHandler() {
				@Override
				public void configure(WebSocketServletFactory factory) {
					factory.register(WebSocketServer.class);
				}
			};

			HandlerList handlers = new HandlerList();
			handlers.addHandler(resourceHandler);
			handlers.addHandler(servletContextHandler);
			handlers.addHandler(wsHandler);

			jettyServer.setHandler(handlers);
			jettyServer.start();
			logger.info("Jetty server started at http://localhost:" + config.getPort());
			jettyServer.join();
		} catch (Exception e) {
			logger.error("Error starting Jetty server", e);
		}
	}

	private static void startWebSocketServer(int port) {
		try {
			WebSocketServerWrapper webSocketServer = new WebSocketServerWrapper(
					new InetSocketAddress("localhost", port));
			webSocketServer.start();
			logger.info("WebSocket server started at ws://localhost:" + port);
		} catch (Exception e) {
			logger.error("Error starting WebSocket server", e);
		}
	}

	private static ValidationSystemProps validateSystemProperties() {
		ValidationSystemProps validationSystemProps = new ValidationSystemProps();

		if (System.getProperty("PORT") == null) {
			validationSystemProps.setMessage("PORT not found.");
		}

		if (System.getProperty("containerBasePath") == null) {
			validationSystemProps.setMessage("Container base path not found.");
		}

		if (System.getProperty("language") == null) {
			validationSystemProps.setMessage("Language vm arg not found.");
		}

		if (validationSystemProps.getMessage().trim().isEmpty()) {
			validationSystemProps.setValid(true);
		}
		return validationSystemProps;
	}

	private static class ValidationSystemProps {
		private boolean valid;
		private String message;

		public ValidationSystemProps() {
			this.valid = false;
			this.message = "";
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isValid() {
			return valid;
		}

		public String getMessage() {
			return message;
		}
	}

}
