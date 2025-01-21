package com.browxy.wrapper.server.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendStaticFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String staticFilePath;

	public SendStaticFileServlet(String staticFilePath) {
		this.staticFilePath = staticFilePath;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		if (requestUri.startsWith("/api")) {
			req.getRequestDispatcher(req.getRequestURI()).forward(req, resp);
			return;
		}

		File staticFile = new File(this.staticFilePath);

		if (staticFile.exists()) {
			resp.setContentType("text/html");
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(new String(Files.readAllBytes(staticFile.toPath())));
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Static file not found");
		}
	}

}
