package com.browxy.wrapper.server.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.response.ResponseMessageUtil;

public class DownloadAssetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DownloadAssetServlet.class);
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
		try {
			String path = alias != null && !alias.trim().isEmpty() ? downloadPath + File.separator + alias
					: downloadPath;
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
		} catch (Exception e) {
			logger.error("error downloading asset file", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseMessageUtil.getStatusMessage("unable to download asset", 500));
		} finally {
			response.flushBuffer();
			if (response.getOutputStream() != null) {
				response.getOutputStream().close();
			}
			if (response.getWriter() != null) {
				response.getWriter().close();
			}
		}
	}
}
