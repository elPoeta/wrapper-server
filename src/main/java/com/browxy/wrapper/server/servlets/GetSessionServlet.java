package com.browxy.wrapper.server.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.response.ResponseMessageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GetSessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GetSessionServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		JsonObject json = new JsonObject();
		try {
			HttpSession session = request.getSession(false);
			response.setStatus(HttpServletResponse.SC_OK);
			if (session != null) {
				String user = (String) session.getAttribute("user");
	            json.addProperty("statusCode", 200);
				json.addProperty("user", user);
				response.getWriter().write(new Gson().toJson(json));
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				json.addProperty("statusCode", 404);
				json.add("user", null);
				response.getWriter().write("No session data found.");
			}

		} catch (Exception e) {
			logger.error("get session error ", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try {
				response.getWriter().write(ResponseMessageUtil.getStatusMessage("Error reading session", 400));
			} catch (IOException e1) {
				logger.error("error response session ", e);
			}
		} finally {
			try {
				response.flushBuffer();
				response.getWriter().close();
			} catch (IOException e) {
				logger.error("error close response session", e);
			}

		}
	}
}
