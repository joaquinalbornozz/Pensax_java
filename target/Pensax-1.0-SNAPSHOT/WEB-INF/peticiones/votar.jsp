<%-- 
    Document   : votar
    Created on : 11 nov 2024, 13:29:15
    Author     : users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Votar Petición</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home.jsp">
                <img src="${pageContext.request.contextPath}/assets/logo.png" alt="Logo" class="h-14 w-auto">
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="./peticiones">Peticiones</a>
                    </li>
                    <%-- Conditionally show admin links --%>
                    <c:if test="${sessionScope.user.getRol().equals(\"admin\")}">
                        <li class="nav-item">
                            <a class="nav-link" href="./carreras">Carreras</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="./usuarios">Usuarios</a>
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
<div class="container my-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h2 class="mb-0">Votar Petición</h2>
        </div>
        <div class="card-body">
            <h3 class="card-title">${peticion.getTitulo()}</h3>
            <p class="card-text">${peticion.getDescripcion()}</p>

            <div class="mt-4 mb-4">
                <h5>Resultados de los votos</h5>
                <p>A favor: <strong>${peticion.getPositivos()}</strong></p>
                <p>En contra: <strong>${peticion.getNegativos()}</strong></p>
            </div>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    ${successMessage}
                </div>
            </c:if>

            <c:if test="${!uservote}">
                <form id="vote-form" method="post" action="./votar?id=${peticion.getIdpeticion()}">
                    <div class="mb-3">
                        <label class="form-label">¿Qué posición tomas frente a esta petición?</label>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" id="positivo" name="voto" value="1">
                            <label class="form-check-label" for="positivo">A favor</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" id="negativo" name="voto" value="0">
                            <label class="form-check-label" for="negativo">En contra</label>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">¿Voto anónimo?</label>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="anonymous" name="anonimo" value="1">
                            <label class="form-check-label" for="anonymous">Sí</label>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Votar</button>
                </form>
            </c:if>

            <c:if test="${uservote}">
                <div class="alert alert-info mt-4">
                    Ya has votado. Gracias por tu participación.
                </div>
            </c:if>

            <c:if test="${usuario == peticion.getUserIdusers() || usuario.getRol().equals(\"admin\")}">
                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/peticiones/show?id=${peticion.getIdpeticion()}" class="text-decoration-none text-primary">
                        Detalles de la Petición
                    </a>
                </div>
            </c:if>
        </div>
            <!-- comment 
        <c:if test="${not empty peticion.getImagen()}">
            <div class="card-footer bg-white">
                <img src="${peticion.getImagen().startsWith('http') ? peticion.getImagen() : ""}" 
                     alt="${peticion.getTitulo()}" 
                     class="rounded img-fluid w-100" style="max-height: 300px; object-fit: cover;">
            </div>
        </c:if>
            -->
    </div>
</div>
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
