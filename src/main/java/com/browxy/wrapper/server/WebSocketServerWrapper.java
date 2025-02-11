package com.browxy.wrapper.server;

import org.java_websocket.WebSocket;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.message.SocketMessage;
import com.browxy.wrapper.response.ResponseHandler;
import com.browxy.wrapper.status.StatusMessageResponse;
import com.browxy.wrapper.server.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.java_websocket.handshake.ClientHandshake;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class WebSocketServerWrapper extends WebSocketServer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerWrapper.class);
	private Gson gson;

	public WebSocketServerWrapper(InetSocketAddress address) {
		super(address);
		Config config = Config.getInstance();
        if(config.isSecure()) {
        	this.HandleSecureConnections(config);
        }
		this.gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	}
	
	private void HandleSecureConnections(Config config) {
		try {
			String keystorePath = config.getKeystorePath();
			String keystorePassword = config.getKeystorePassword();

			KeyStore keystore = KeyStore.getInstance("PKCS12");
			keystore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keystore, keystorePassword.toCharArray());

			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystore);

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

			this.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		logger.info("Open connection: " + conn.getLocalSocketAddress());
		JsonObject json = new JsonObject();
		json.addProperty("remoteAddress", conn.getRemoteSocketAddress().getAddress().toString());
		json.addProperty("isOpen", conn.isOpen());
		json.addProperty("type", "open");
		conn.send(this.gson.toJson(json));

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		logger.info("Closed connection: " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		logger.info(message);
		SocketMessage responseBuilder = this.gson.fromJson(message, SocketMessage.class);
		ResponseHandler responseHandler = new ResponseHandler(responseBuilder.getPayload());
		String result = this.buildResponse(responseHandler, responseBuilder.getType());

		conn.send(result);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error("", ex);
		String errorMessage = ex.getMessage() != null || !ex.getMessage().trim().equals("") ? ex.getMessage()
				: "An error has occurred in the connection";
		StatusMessageResponse errorMessageResponse = StatusMessageResponse.getInstance();
		errorMessageResponse.setMessage(errorMessage);
		conn.send(this.gson.toJson(errorMessageResponse, StatusMessageResponse.class));
	}

	@Override
	public void onStart() {
		logger.info("WebSocket server started successfully");
	}

	private String buildResponse(ResponseHandler responseHandler, String type) {
		SocketMessage responseBuilder = new SocketMessage(responseHandler.getResponse(), type);
		return this.gson.toJson(responseBuilder, SocketMessage.class);
	}
}
