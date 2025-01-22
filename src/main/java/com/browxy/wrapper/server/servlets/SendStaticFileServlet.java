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
    private String entryPoint;
    
	public SendStaticFileServlet(String staticFilePath, String entryPoint) {
		this.staticFilePath = staticFilePath;
		this.entryPoint = entryPoint;
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
			String html = new String(Files.readAllBytes(staticFile.toPath()));
			html = html.replace("%%SOCKET_PORT%%", System.getProperty("PORT"))
			.replace("%%ENTRY_POINT%%", this.entryPoint);
			resp.getWriter().write(html);
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Static file not found");
		}
	}

}
