package com.browxy.wrapper.server.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.browxy.wrapper.response.ResponseMessageUtil;

public class DownloadAssetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final String downloadPath;

	public DownloadAssetServlet(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileName = request.getParameter("file");
		String alias = request.getParameter("alias");
		if (fileName == null || fileName.trim().isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseMessageUtil.getStatusMessage("File name is missing", 400));
			return;
		}
		String path = alias != null && !alias.trim().isEmpty() ? downloadPath + File.separator + alias : downloadPath;
		File file = new File(path, fileName);
		if (!file.exists() || !file.isFile()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseMessageUtil.getStatusMessage("File not found", 400));
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		FileUtils.copyFile(file, response.getOutputStream());
	}
}
