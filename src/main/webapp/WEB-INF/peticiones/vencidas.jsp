<%-- 
    Document   : vencidas
    Created on : 13 nov 2024, 10:08:09
    Author     : users
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Peticiones Vencidas</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
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

    <div class="container my-5 p-4 bg-light rounded shadow-lg">
        <h2 class="font-weight-bold text-center mb-4">Peticiones Vencidas</h2>

        <c:choose>
            <c:when test="${not empty peticiones}">
                <div class="row">
                    <c:forEach var="peticion" items="${peticiones}">
                        <div class="col-md-6 mb-4">
                            <a href="${pageContext.request.contextPath}/peticiones/show?id=${peticion.getIdpeticion()}" class="text-decoration-none">
                                <div class="card h-100 shadow-sm">
                                    <div class="card-body">
                                        <h5 class="card-title text-dark font-weight-bold">${peticion.getTitulo()}</h5>
                                        <p class="card-text text-muted">
                                            ${fn:substring(peticion.getDescripcion(), 0, 50)}...
                                        </p>
                                        <p class="text-muted mb-2">Vencimiento: <fmt:formatDate value="${peticion.getVencimiento()}" pattern="dd/MM/yyyy" /></p>
                                    </div>
                                    <c:if test="${not empty peticion.getImagen()}">
                                        <c:choose>
                                            <c:when test="${peticion.getImagen().startsWith('http')}">
                                                <!-- Imagen de URL externa -->
                                                <img src="${peticion.getImagen()}" alt="${peticion.getTitulo()}" class="card-img-bottom" style="max-height: 150px; object-fit: cover;">
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Imagen almacenada en el servidor -->
                                                <img src="http://localhost:8080/Pensax/images?imageName=${peticion.getImagen()}" class="card-img-bottom" alt="${peticion.getTitulo()}" style="max-height: 150px; object-fit: cover;">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-secondary text-center">No hay peticiones vencidas</div>
            </c:otherwise>
        </c:choose>

        
    </div>
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
