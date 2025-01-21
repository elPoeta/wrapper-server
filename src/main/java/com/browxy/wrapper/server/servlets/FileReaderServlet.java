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
			if (file == null || file.trim().isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						getResponseMesage("Request does not contain file", 404));
			}
            String fullPath = this.basePath + File.separator + file; 
			String content = FileManager.readFile(fullPath, "UTF-8");
			JsonObject json = new JsonObject();
			json.addProperty("statusCode", 200);
			json.addProperty("content", content);
			response.getWriter().write(new Gson().toJson(json));

		} catch (Exception e) {
			logger.error("FileReader error ", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, getResponseMesage("Error reading file", 404));
		}
	}

	private String getResponseMesage(String message, int statusCode) {
		StatusMessageResponse messageResponse = StatusMessageResponse.getInstance();
		messageResponse.setStatusCode(statusCode);
		messageResponse.setMessage(message);
		return new Gson().toJson(messageResponse, StatusMessageResponse.class);
	}

}
