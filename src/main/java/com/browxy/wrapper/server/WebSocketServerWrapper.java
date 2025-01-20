package com.browxy.wrapper.server;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.response.ResponseHandler;
import com.browxy.wrapper.status.StatusMessageResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

public class WebSocketServerWrapper extends WebSocketServer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerWrapper.class);
	private Gson gson;

	public WebSocketServerWrapper(InetSocketAddress address) {
		super(address);
		this.gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		JsonObject json = new JsonObject();
		json.addProperty("remoteAddress", conn.getRemoteSocketAddress().toString());
		json.addProperty("isOpen", conn.isOpen());
		conn.send(this.gson.toJson(json));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		logger.error("Closed connection: ", conn.getRemoteSocketAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		logger.info(message);
		ResponseHandler responseHandler = new ResponseHandler(message);
		String result = responseHandler.getResponse();
		conn.send(result);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error("",ex);
		String errorMessage = ex.getMessage() != null || !ex.getMessage().trim().equals("") ? ex.getMessage() : "An error has occurred in the connection";
		StatusMessageResponse errorMessageResponse = StatusMessageResponse.getInstance();
		errorMessageResponse.setMessage(errorMessage);
		conn.send(this.gson.toJson(errorMessageResponse, StatusMessageResponse.class));
	}

	@Override
	public void onStart() {
		logger.info("WebSocket server started successfully");
	}
}
