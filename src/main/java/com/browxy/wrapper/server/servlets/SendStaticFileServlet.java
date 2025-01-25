package com.browxy.wrapper.server.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browxy.wrapper.fileUtils.MimeTypeUtil;

public class SendStaticFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SendStaticFileServlet.class);

	private String staticDirPath;
	private String staticFilePath;
	private String entryPoint;

	public SendStaticFileServlet(String staticDirPath, String staticFilePath, String entryPoint) {
		this.staticDirPath = staticDirPath;
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
		try {
			String filePath = requestUri.startsWith("/assets") ? this.staticDirPath + requestUri
					: this.staticDirPath + File.separator + this.staticFilePath;
			File file = new File(filePath);
			String mimeType = MimeTypeUtil.getMimeTypeByFileName(file.getName());
			if (file.exists()) {
				String content = new String(Files.readAllBytes(file.toPath()));
				if (mimeType.equals("text/html")) {
					String hostSocketPort = System.getenv("HOST_SOCKET_PORT") != null
							? System.getenv("HOST_SOCKET_PORT")
							: System.getProperty("PORT");
					content = content.replace("%%SOCKET_PORT%%", hostSocketPort).replace("%%ENTRY_POINT%%",
							this.entryPoint);
				}
				resp.setContentType(mimeType);
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().write(content);
			} else {
				resp.setContentType("text/html");
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Static file not found");
			}
		} catch (Exception e) {
			logger.error("unable to send static content", e);
			resp.setContentType("text/html");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "unable to send static content");
		} finally {
			resp.flushBuffer();
			resp.getWriter().close();
		}
	}

}
