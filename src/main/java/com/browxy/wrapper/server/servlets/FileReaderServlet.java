package com.browxy.wrapper.server.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.fileUtils.FileManager;
import com.browxy.wrapper.status.StatusMessageResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FileReaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FileReaderServlet.class);
	private String basePath;

	public FileReaderServlet(String basePath) {
		this.basePath = basePath;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		try {
			String file = request.getParameter("file");
			logger.debug("[read file ",file);
			if (file == null || file.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(getResponseMessage("Request does not contain file", 404));
			}
			String path = request.getParameter("path");
			String fullPath = path == null || path.trim().isEmpty() ? this.basePath + File.separator + file
					: this.basePath + File.separator + path + File.separator + file;
			String content = FileManager.readFile(fullPath, "UTF-8");
			JsonObject json = new JsonObject();
			json.addProperty("statusCode", 200);
			json.addProperty("content", content);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(new Gson().toJson(json));

		} catch (Exception e) {
			logger.error("FileReader error ", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(getResponseMessage("Error reading file", 404));
		}
	}

	private String getResponseMessage(String message, int statusCode) {
		StatusMessageResponse messageResponse = StatusMessageResponse.getInstance();
		messageResponse.setStatusCode(statusCode);
		messageResponse.setMessage(message);
		return new Gson().toJson(messageResponse, StatusMessageResponse.class);
	}

}
