package com.browxy.wrapper.server;

import java.io.File;
import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.server.config.Config;
import com.browxy.wrapper.server.servlets.DownloadAssetServlet;
import com.browxy.wrapper.server.servlets.FileReaderServlet;
import com.browxy.wrapper.server.servlets.FileUploadServlet;
import com.browxy.wrapper.server.servlets.GetAssetServlet;
import com.browxy.wrapper.server.servlets.GetSessionServlet;
import com.browxy.wrapper.server.servlets.AuthServlet;
import com.browxy.wrapper.server.servlets.SendStaticFileServlet;

public class StartWrapperServer {
	private static final Logger logger = LoggerFactory.getLogger(StartWrapperServer.class);

	public static void main(String[] args) throws Exception {
		Config config = Config.getInstance();
		if (config == null) {
			throw new RuntimeException("Server config not loaded...");
		}
		String containerBasePath = config.getContainerBasePath();
		System.setProperty("java.class.path", containerBasePath + File.separator + "target/classes");

		Thread jettyThread = new Thread(() -> startJettyServer(config, containerBasePath));
		jettyThread.start();

		int webSocketPort = config.getSocketPort();
		Thread webSocketThread = new Thread(() -> startWebSocketServer(webSocketPort));
		webSocketThread.start();
	}

	private static void startJettyServer(Config config, String containerBasePath) {
		try {
			Server jettyServer = new Server(config.getServerPort());
			ServletContextHandler servletContextHandler = getServletHandler(config);

			WebSocketHandler wsHandler = new WebSocketHandler() {
				@Override
				public void configure(WebSocketServletFactory factory) {
					factory.register(WebSocketServer.class);
				}
			};

			HandlerList handlers = new HandlerList();
			handlers.addHandler(servletContextHandler);
			handlers.addHandler(wsHandler);

			jettyServer.setHandler(handlers);

			jettyServer.start();
			logger.info("Jetty server started at http://localhost:" + config.getSocketPort());
			jettyServer.join();
		} catch (Exception e) {
			logger.error("Error starting Jetty server", e);
		}
	}

	private static void startWebSocketServer(int port) {
		try {
			WebSocketServerWrapper webSocketServer = new WebSocketServerWrapper(new InetSocketAddress(port));
			webSocketServer.start();
			logger.info("WebSocket server started at ws://localhost:" + port);
		} catch (Exception e) {
			logger.error("Error starting WebSocket server", e);
		}
	}

	private static ServletContextHandler getServletHandler(Config config) {
		String basePath = config.getContainerBasePath();
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.getSessionHandler().setMaxInactiveInterval(60 * 60);
		servletContextHandler.addServlet(
				new ServletHolder(new SendStaticFileServlet(basePath + File.separator + config.getStaticDir(),
						config.getStaticFile(), config.getEntryPoint())),
				"/*");
		servletContextHandler.addServlet(new ServletHolder(new FileUploadServlet(config.getStorage())),
				"/api/v1/upload");
		servletContextHandler.addServlet(new ServletHolder(new GetAssetServlet(config.getStorage())),
				"/api/v1/getAsset");
		servletContextHandler.addServlet(new ServletHolder(new DownloadAssetServlet(config.getStorage())),
				"/api/v1/downloadAsset");
		servletContextHandler.addServlet(new ServletHolder(new FileReaderServlet(basePath)), "/api/v1/readFile");
		servletContextHandler.addServlet(new ServletHolder(new GetSessionServlet()), "/api/v1/getSession");
		servletContextHandler.addServlet(new ServletHolder(new AuthServlet()), "/api/v1/auth");
		servletContextHandler.setContextPath("/");
		return servletContextHandler;
	}

}
