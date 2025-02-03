package com.browxy.wrapper.server.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.db.DBManager;
import com.browxy.wrapper.db.repository.impl.UserRepositoryImpl;
import com.browxy.wrapper.db.service.impl.UserServiceImpl;
import com.browxy.wrapper.model.User;
import com.browxy.wrapper.response.ResponseMessageUtil;
import com.browxy.wrapper.server.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);

	private Gson gson;

	public AuthServlet() {
		this.gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	    JsonObject json = new JsonObject();
	    response.setContentType("application/json");  
	    try {
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.removeAttribute("user");  
	        }
	        response.setStatus(HttpServletResponse.SC_OK);
	        json.addProperty("statusCode", 200);
	        response.getWriter().write(new Gson().toJson(json));

	    } catch (Exception e) {
	        logger.error("Error processing session", e);
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        try {
	            response.getWriter().write(ResponseMessageUtil.getStatusMessage("Error reading session", 400));
	        } catch (IOException e1) {
	            logger.error("Error writing response", e1);
	        }
	    } finally {
	        try {
	            response.flushBuffer();
	            response.getWriter().close();
	        } catch (IOException e) {
	            logger.error("Error closing response", e);
	        }
	    }
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		JsonObject json = new JsonObject();
		response.setContentType("application/json");  
		try {
			String body = request.getReader().readLine();

			User user = gson.fromJson(body, User.class);
			Config config = Config.getInstance();

			DBManager dbManager = DBManager.getInstance(config.getDataSourceUserName(), config.getDataSourcePassword(),
					config.getDataSourceUrl("jdbc:mysql", "UTF-8"), config.getDataSourceDbName());

			UserRepositoryImpl userRepository = new UserRepositoryImpl(dbManager);
			UserServiceImpl userService = new UserServiceImpl(userRepository);

			List<User> users = userService.getByCustom("SELECT * from users where email = ?",
					Arrays.asList(user.getEmail()), true);
			User userDb = null;
			if (!users.isEmpty()) {
				userDb = users.get(0);
			}
			if (userDb == null || !userDb.getPassword().equals(user.getPassword())) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(ResponseMessageUtil.getStatusMessage("Bad credentials", 400));
				return;
			}
			userDb.setPassword("");
			HttpSession session = request.getSession(true);
			session.setAttribute("user", new Gson().toJson(userDb));
			json.addProperty("statusCode", 200);
			json.add("user", gson.toJsonTree(userDb));
			response.setStatus(HttpServletResponse.SC_OK);
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
