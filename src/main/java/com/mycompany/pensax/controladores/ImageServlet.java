package com.mycompany.pensax.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.Files;

@WebServlet("/images/*") // Map this servlet to /images/*
public class ImageServlet extends HttpServlet {

    // Define the base directory where images are stored on the server
    private static final String IMAGE_DIRECTORY = "C:/Users/users/Documents/Cristiannika/TERCER AnO FACULTAD/PROGRAMACION WEB III/Pensax/src/main/webapp/WEB-INF/uploads/imagenes"; // Adjust to your actual path
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el nombre de la imagen desde el parámetro de la solicitud
        String imageName = request.getParameter("imageName");

        // Construir la ruta completa de la imagen
        File imageFile = new File(IMAGE_DIRECTORY, imageName);

        if (imageFile.exists() && imageFile.isFile()) {
            response.setContentType("image/jpeg");
            Files.copy(imageFile.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La imagen no fue encontrada en el servidor.");
        }
    }
}
