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

public class GetSessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GetSessionServlet.class);
    private Gson gson;
    
    public GetSessionServlet() {
	  this.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
	}
	
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		JsonObject json = new JsonObject();
		response.setContentType("application/json"); 
		try {
			HttpSession session = request.getSession(false);
			response.setStatus(HttpServletResponse.SC_OK);
			if (session != null) {
				String userSession = (String) session.getAttribute("user");
				if (userSession != null) {
					User user = this.gson.fromJson(userSession, User.class);
					json.addProperty("statusCode", 200);
					json.add("user", this.gson.toJsonTree(user));
					response.getWriter().write(this.gson.toJson(json));
					return;
				}
			}
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			json.addProperty("statusCode", 404);
			json.add("user", null);
			response.getWriter().write(this.gson.toJson(json));

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
