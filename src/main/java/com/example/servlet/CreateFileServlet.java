package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@MultipartConfig
public class CreateFileServlet extends HttpServlet {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization");
            return;
        }

        String decoded = new String(
                Base64.getDecoder().decode(authHeader.substring(6))
        );
        String[] creds = decoded.split(":");

        if (!USERNAME.equals(creds[0]) || !PASSWORD.equals(creds[1])) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Credentials");
            return;
        }

        String uploadDir = "C:/Users/Axeno/Documents/uploaded-files";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Part filePart = request.getPart("file");
        String originalFileName = filePart.getSubmittedFileName();

        File file = new File(uploadDir, originalFileName);
        if (file.exists()) {
            String name = originalFileName;
            String base = name.contains(".") ? name.substring(0, name.lastIndexOf('.')) : name;
            String ext = name.contains(".") ? name.substring(name.lastIndexOf('.')) : "";
            file = new File(uploadDir, base + "_" + System.currentTimeMillis() + ext);
        }

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        }

        response.getWriter().println("File uploaded successfully: " + file.getName());
    }
}
