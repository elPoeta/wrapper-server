package com.browxy.wrapper.server.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.model.User;
import com.browxy.wrapper.response.ResponseMessageUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	private Gson gson;
	
	public LoginServlet() {
		this.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		JsonObject json = new JsonObject();
		try {
			String body = request.getReader().readLine();
	            
	        User user = gson.fromJson(body, User.class);
	        
			HttpSession session = request.getSession(true);
			session.setAttribute("user", "john_doe");
			json.addProperty("statusCode", 200);
			json.add("user", gson.toJsonTree(user));
			response.getWriter().write(new Gson().toJson(json));

		} catch (Exception e) {
			logger.error("get session error ", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try {
				response.getWriter().write(ResponseMessageUtil.getStatusMessage("Error setting session", 400));
			} catch (IOException e1) {
				logger.error("error response set session ", e);
			}
		} finally {
			try {
				response.flushBuffer();
				response.getWriter().close();
			} catch (IOException e) {
				logger.error("error close response set session", e);
			}

		}
	}
}
