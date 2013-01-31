package com.thoughtworks.i1.fileUploader.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.i1.fileUploader.domain.UploadResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final Logger LOGGER = Logger.getLogger(UploadServlet.class.toString());

    @Inject
    @Named("file.upload.directory")
    private String fileUploadDirectory;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("call POST with multipart form data");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }

        PrintWriter writer = response.getWriter();
        response.setContentType("text/plain");
        try {
            ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> items = uploadHandler.parseRequest(request);
            FileItem fileItem = getFileItem(items);
            File file = new File(fileUploadDirectory, fileItem.getName());
            fileItem.write(file);
            writer.write(new UploadResult(fileItem).asJSON());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to save file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    private FileItem getFileItem(List<FileItem> items) {
        for (FileItem item : items) {
            if (!item.isFormField()) {
                return item;
            }
        }
        throw new RuntimeException("");
    }
}
