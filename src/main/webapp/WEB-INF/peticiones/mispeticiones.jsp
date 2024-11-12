<%-- 
    Document   : mispeticiones
    Created on : 11 nov 2024, 21:19:38
    Author     : users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Peticiones</title>
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
    <%-- Verificar si el usuario es administrador --%>
    <c:if test="${sessionScope.user.getRol().equals(\"admin\")}">
        <nav class="bg-dark p-2">
            <div class="container">
                <ul class="nav">
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
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/peticiones/eliminadas" class="nav-link ${pageContext.request.requestURI.endsWith('/peticiones/eliminadas') ? 'text-warning' : 'text-muted'}">
                            Eliminadas
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </c:if>

    <%-- Verificar si el usuario es redactor --%>
    <c:if test="${sessionScope.user.getRol().equals(\"redactor\")}">
        <nav class="bg-dark p-2">
            <div class="container">
                <ul class="nav">
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

    <div class="container my-5 p-4 bg-white rounded shadow">
        <!-- Header -->
        <h2 class="text-center text-dark mb-4">Mis Peticiones</h2>
        
        <!-- Mensaje de éxito -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success font-weight-bold">
                ${successMessage}
            </div>
        </c:if>

        <!-- Filtro de Peticiones -->
        <div class="mb-4">
            <form method="GET" action="mis" class="d-flex justify-content-center">
                <select name="filter" class="form-select w-25" onchange="this.form.submit()">
                    <option value="">Todas</option>
                    <option value="publicadas" ${param.filter == 'publicadas' ? 'selected' : ''}>Publicadas</option>
                    <option value="rechazadas" ${param.filter == 'rechazadas' ? 'selected' : ''}>Rechazadas</option>
                    <option value="eliminadas" ${param.filter == 'eliminadas' ? 'selected' : ''}>Eliminadas</option>
                    <option value="pendientes" ${param.filter == 'pendientes' ? 'selected' : ''}>Pendientes</option>
                    <option value="vencidas" ${param.filter == 'vencidas' ? 'selected' : ''}>Vencidas</option>
                </select>
            </form>
        </div>
                        <jsp:useBean id="now" class="java.util.Date" scope="request" />

        <!-- Lista de Peticiones -->
        <div class="list-group">
            <c:forEach var="peticion" items="${peticiones}">
                <div class="list-group-item bg-light mb-3 rounded shadow-sm">
                    <h5 class="mb-1 text-dark">${peticion.titulo}</h5>
                    <p class="text-muted">${peticion.descripcion}</p>
                    <p class="text-secondary">Vencimiento: <fmt:formatDate value="${peticion.vencimiento}" pattern="dd/MM/yyyy" /></p>

                    <c:choose>
                        <c:when test="${peticion.getDeleted()==1}">
                            <p class="text-danger">Eliminada</p>
                            <a href="./ver-peticion?id=${peticion.getIdpeticion()}" class="text-primary text-decoration-underline">Ver</a>
                        </c:when>
                        <c:when test="${peticion.getVencimiento().before(now)}">
                            <p class="text-warning">Vencida</p>
                            <a href="./ver-peticion?id=${peticion.getIdpeticion()}" class="text-primary text-decoration-underline">Ver</a>
                        </c:when>
                        <c:when test="${peticion.getPublicada()==1}">
                            <p class="text-success">Publicada</p>
                            <a href="./ver-peticion?id=${peticion.getIdpeticion()}" class="text-primary text-decoration-underline">Ver</a>
                        </c:when>
                        <c:when test="${peticion.getRechazada()==1}">
                            <p class="text-danger">Rechazada</p>
                            <a href="./edit?id=${peticion.getIdpeticion()}" class="text-primary text-decoration-underline">Editar</a>
                        </c:when>
                        <c:otherwise>
                            <p class="text-info">Pendiente</p>
                            <a href="./edit?id=${peticion.getIdpeticion()}" class="text-primary text-decoration-underline">Editar</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
            
            <!-- Mensaje si no hay peticiones -->
            <c:if test="${peticiones == null || peticiones.isEmpty()}">
                <p class="text-center text-dark">No hay peticiones disponibles</p>
            </c:if>
        </div>

        <!-- Footer -->
    
    </div>
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now1" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now1}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
