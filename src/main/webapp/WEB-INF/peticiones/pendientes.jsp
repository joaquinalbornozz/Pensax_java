<%-- 
    Document   : pendientes
    Created on : 13 nov 2024, 3:44:49
    Author     : users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Peticiones</title>
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
                <ul class="nav navbar-light bg-light">
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

    <div class="container my-4 p-4 bg-white rounded shadow-lg">
        <!-- Success Message -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success font-weight-bold mb-4">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <!-- Error Message -->
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger font-weight-bold mb-4">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>
        <div class="text-center display-4 font-weight-bold mb-5 text-dark">Peticiones Pendientes</div>

        <c:forEach var="peticion" items="${peticiones}">
            <div class="bg-light shadow-sm rounded p-4 d-flex align-items-center justify-content-between mb-3">
                <div>
                    <h5 class="font-weight-bold text-dark">${peticion.getTitulo()}</h5>
                    <p class="text-muted">${fn:substring(peticion.getDescripcion(), 0, 50)}...</p>
                    <p class="text-secondary">Vencimiento: <fmt:formatDate value="${peticion.getVencimiento()}" pattern="dd/MM/yyyy" /></p>
                </div>
                <div class="ml-4 d-flex gap-2">
                    <a href="./aprobar?id=${peticion.getIdpeticion()}" class="btn btn-primary">Ver Detalles</a>
                </div>
            </div>
        </c:forEach>

        <!-- No Peticiones Placeholder -->
        <c:if test="${empty peticiones}">
            <div class="bg-light shadow-sm rounded p-4 text-center">
                <p class="text-muted">No hay peticiones pendientes</p>
            </div>
        </c:if>
    </div>
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
    <!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
