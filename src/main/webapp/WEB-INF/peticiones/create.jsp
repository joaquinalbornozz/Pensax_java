<%-- 
    Document   : create
    Created on : 11 nov 2024, 17:57:32
    Author     : users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Nueva Petición</title>
    <!-- Agrega el enlace a Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/peticiones">
                <img src="http://localhost:8080/Pensax/images?imageName=/logo.png" alt="Logo" style="height: 50px;width: auto;">
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/peticiones">Peticiones</a>
                    </li>
                    <%-- Conditionally show admin links --%>
                    <c:if test="${sessionScope.user.getRol().equals(\"admin\")}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/carreras">Carreras</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                        </li>
                    </c:if>
                </ul>
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <%= session.getAttribute("userFullName") %>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="./profile.jsp">Perfil</a>
                            <a class="dropdown-item" href="./logout">Salir</a>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <%-- Verificar si el usuario es administrador --%>
    <c:if test="${sessionScope.user.getRol().equals(\"admin\")}">
        <nav class="bg-light p-2">
            <div class="container">
                <ul class="nav navbar-light bg-light">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones') ? 'text-warning' : 'text-muted'}">
                            Publicadas
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones/pendientes" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones/pendientes') ? 'text-warning' : 'text-muted'}">
                            Pendientes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones/vencidas" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones/vencidas') ? 'text-warning' : 'text-muted'}">
                            Vencidas
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </c:if>

    <%-- Verificar si el usuario es redactor --%>
    <c:if test="${sessionScope.user.getRol().equals(\"redactor\")}">
        <nav class="bg-light p-2">
            <div class="container">
                <ul class="navnavbar-light bg-light">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones') ? 'text-warning' : 'text-muted'}">
                            Publicadas
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones/create" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones/create') ? 'text-warning' : 'text-muted'}">
                            Crear Petición
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones/mis" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones/mis') ? 'text-warning' : 'text-muted'}">
                            Mis Peticiones
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </c:if>

<div class="container my-5 p-4 bg-light rounded shadow">
    <h2 class="text-center text-primary mb-4">Crear Nueva Petición</h2>

    <!-- Mensaje de errores si existen -->
    <c:if test="${error.trim.notEmpty()}">
        <div class="alert alert-danger">
            <p>${error}</p>
        </div>
    </c:if>

    <!-- Formulario para crear la petición -->
    <form method="post" action="./create" enctype="multipart/form-data">
        
        <div class="form-group">
            <label for="titulo" class="font-weight-bold">Título</label>
            <input type="text" name="titulo" id="titulo" value="${param.titulo}" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="descripcion" class="font-weight-bold">Descripción</label>
            <textarea name="descripcion" id="descripcion" rows="4" class="form-control" required>${param.descripcion}</textarea>
        </div>

        <div class="form-group">
            <label class="font-weight-bold">Imagen</label>
            <div class="form-check">
                <input type="radio" id="file_choice" name="image_choice" value="file" class="form-check-input" checked>
                <label for="file_choice" class="form-check-label">Subir archivo</label>
            </div>
            <div class="form-check">
                <input type="radio" id="url_choice" name="image_choice" value="url" class="form-check-input">
                <label for="url_choice" class="form-check-label">Enlace URL</label>
            </div>
        </div>

        <div class="form-group" id="file_input">
            <label for="imagen" class="font-weight-bold">Archivo de imagen</label>
            <input type="file" name="imagen" id="imagen" class="form-control-file">
        </div>

        <div class="form-group" id="url_input" style="display: none;">
            <label for="imagen_url" class="font-weight-bold">Enlace URL de la imagen</label>
            <input type="text" name="imagen_url" id="imagen_url" class="form-control">
        </div>

        <div class="form-group">
            <label for="vencimiento" class="font-weight-bold">Fecha de Vencimiento</label>
            <input type="date" name="vencimiento" id="vencimiento" value="${param.vencimiento}" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="carrera_id" class="font-weight-bold">Carrera (Opcional)</label>
            <select name="carrera_id" id="carrera_id" class="form-control">
                <option value="">Selecciona una carrera</option>
                <c:forEach var="carrera" items="${carreras}">
                    <option value="${carrera.getIdCarrera()}" ${carrera.getIdCarrera() == param.carrera_id ? 'selected' : ''}>${carrera.getTitulo()}</option>
                </c:forEach>
            </select>
        </div>

        <div class="text-right">
            <button type="submit" class="btn btn-primary">Crear Petición</button>
        </div>
    </form>
</div>
<!-- Footer -->
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
<!-- Agrega los enlaces a Bootstrap y jQuery -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const fileChoice = document.getElementById('file_choice');
        const urlChoice = document.getElementById('url_choice');
        const fileInput = document.getElementById('file_input');
        const urlInput = document.getElementById('url_input');

        fileChoice.addEventListener('change', function () {
            fileInput.style.display = 'block';
            urlInput.style.display = 'none';
        });

        urlChoice.addEventListener('change', function () {
            fileInput.style.display = 'none';
            urlInput.style.display = 'block';
        });
    });
</script>

</body>
</html>
