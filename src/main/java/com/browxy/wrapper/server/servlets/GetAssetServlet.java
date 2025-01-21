package com.browxy.wrapper.server.servlets;

import org.apache.commons.io.FileUtils;

import com.browxy.wrapper.status.StatusMessageResponse;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class GetAssetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final String downloadPath;

	public GetAssetServlet(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileName = request.getParameter("file");
		String alias = request.getParameter("alias");
		if (fileName == null || fileName.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, getResponseMesage("File name is missing", 400));
			return;
		}
		String path = alias != null && !alias.trim().isEmpty() ? downloadPath + File.separator + alias : downloadPath;
		File file = new File(path, fileName);
		if (!file.exists() || !file.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, getResponseMesage("File not found", 400));
			return;
		}

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		FileUtils.copyFile(file, response.getOutputStream());
	}

	private String getResponseMesage(String message, int statusCode) {
		StatusMessageResponse errorMessageResponse = StatusMessageResponse.getInstance();
		errorMessageResponse.setStatusCode(statusCode);
		errorMessageResponse.setMessage(message);
		return new Gson().toJson(errorMessageResponse, StatusMessageResponse.class);
	}
}
