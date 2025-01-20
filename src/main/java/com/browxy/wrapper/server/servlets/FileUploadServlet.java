package com.browxy.wrapper.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.browxy.wrapper.status.StatusMessageResponse;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String uploadPath;

	public FileUploadServlet(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					getResponseMesage("Request does not contain upload data", 400));
			return;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();

		File repository = new File(System.getProperty("java.io.tmpdir"));
		factory.setRepository(repository);

		ServletFileUpload upload = new ServletFileUpload(factory);

		upload.setFileSizeMax(50 * 1024 * 1024); // 50MB max file size
		upload.setSizeMax(100 * 1024 * 1024); // 100MB max total size
		int code = 400;
		String message = "file/s uploaded OK";
		try {
			List<FileItem> formItems = upload.parseRequest(request);
			String alias = request.getAttribute("alias").toString();
			for (FileItem item : formItems) {
				if (!item.isFormField()) {
					String fileName = new File(item.getName()).getName();
					String filePath = uploadPath + File.separator + alias + File.separator + fileName;
					File storeFile = new File(filePath);
					item.write(storeFile);
				}
			}
			code = 200;
		} catch (FileUploadBase.SizeLimitExceededException e) {
			message = "File size exceeds the limit!";
		} catch (Exception e) {
			message = "Error while uploading file: " + e.getMessage();
		} finally {
			response.getWriter().write(getResponseMesage(message, code));
		}
	}

	private String getResponseMesage(String message, int statusCode) {
		StatusMessageResponse errorMessageResponse = StatusMessageResponse.getInstance();
		errorMessageResponse.setStatusCode(statusCode);
		errorMessageResponse.setMessage(message);
		return new Gson().toJson(errorMessageResponse, StatusMessageResponse.class);
	}
}
