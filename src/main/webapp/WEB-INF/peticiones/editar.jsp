<%-- 
    Document   : editar
    Created on : 12 nov 2024, 22:24:58
    Author     : users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Petición</title>
    <!-- Agrega el enlace a Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="nav bar-brand" href="${pageContext.request.contextPath}/peticiones">
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
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Perfil</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Salir</a>
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

<div class="container my-5 p-4 bg-white rounded-lg shadow-lg">
    <h2 class="font-weight-bold text-xl mb-4">Editar Petición</h2>

    <%-- Mostrar errores --%>
    <c:if test="${error.trim.notEmpty()}">
        <div class="alert alert-danger">
            <p>${error}</p>
        </div>
    </c:if>

    <%-- Mostrar comentario de rechazo si existe --%>
    <c:if test="${peticion.getRechazada()==1}">
        <div class="mb-4">
            <label class="font-weight-bold text-danger">Comentario de Rechazo:</label>
            <p class="text-danger">${peticion.getComentario()}</p>
        </div>
    </c:if>

    <form method="POST" action="./editar?id=${peticion.getIdpeticion()}" enctype="multipart/form-data">
        <div class="form-group">
            <label for="titulo">Título</label>
            <input type="text" name="titulo" id="titulo" class="form-control" value="${peticion.getTitulo()}" required>
        </div>

        <div class="form-group">
            <label for="descripcion">Descripción</label>
            <textarea name="descripcion" id="descripcion" class="form-control" rows="4" required>${peticion.getDescripcion()}</textarea>
        </div>

        <div class="form-group">
            <label>Imagen</label>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="image_choice" id="file_choice" value="file" ${!peticion.isImageUrl() ? "checked" : ""}>
                <label class="form-check-label" for="file_choice">Subir archivo</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="image_choice" id="url_choice" value="url" ${peticion.isImageUrl() ? "checked" : ""}>
                <label class="form-check-label" for="url_choice">Enlace URL</label>
            </div>
        </div>

        <div class="form-group" id="file_input" style="${peticion.isImageUrl() ? 'display:none;' : ''}">
            <label for="imagen">Archivo de imagen</label>
            <input type="file" name="imagen" id="imagen" class="form-control-file">
        </div>

        <div class="form-group" id="url_input" style="${!peticion.isImageUrl() ? 'display:none;' : ''}">
            <label for="imagen_url">Enlace URL de la imagen</label>
            <input type="text" name="imagen_url" id="imagen_url" class="form-control" value="${peticion.imagen}">
        </div>

        <div class="form-group">
            <label for="vencimiento">Fecha de Vencimiento</label>
            <input type="date" name="vencimiento" id="vencimiento" class="form-control" value="${peticion.getVencimiento().toString()}" required>
        </div>

        <div class="form-group">
            <label for="carrera_id">Carrera (Opcional)</label>
            <select name="carrera_id" id="carrera_id" class="form-control">
                <option value="">Selecciona una carrera</option>
                <c:forEach var="carrera" items="${carreras}">
                    <option value="${carrera.getIdCarrera()}" ${peticion.getCarreraidCarrera().getIdCarrera() == carrera.getIdCarrera() ? "selected" : ""}>${carrera.getTitulo()}</option>
                </c:forEach>
            </select>
        </div>

        <div class="text-right">
            <button type="submit" class="btn btn-primary">Actualizar Petición</button>
        </div>
    </form>
</div>

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
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    </body>
</html>
