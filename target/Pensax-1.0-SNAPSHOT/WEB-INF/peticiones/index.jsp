<%-- 
    Document   : index
    Created on : 5 nov 2024, 23:06:42
    Author     : users
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Peticiones</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .bg-dark-mode { background-color: #1e1e2f; color: #ddd; }
        .text-dark-mode { color: #ddd; }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-light bg-dark-mode">
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


    <!-- Main Content -->
    <main class="flex-grow-1 container my-4 p-4 bg-white rounded shadow-sm">
        <div class="container">
            <h2 class="font-weight-bold text-primary mb-3">Peticiones</h2>

            <!-- Mensajes de éxito y error -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <c:out value="${success}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${error}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <c:if test="${sessionScope.error!=null && not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${error}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="close"></button>
                </div>
            </c:if>

            <!-- Filtro de carrera -->
            <div class="mb-4">
                <form method="GET" action="${pageContext.request.contextPath}/peticiones" class="d-flex justify-content-center">
                    <select name="carrera_id" class="form-select w-auto" onchange="this.form.submit()">
                        <option value="">Todas</option>
                        <c:forEach var="carrera" items="${carreras}">
                            <option value="${carrera.idCarrera}" ${carrera.idCarrera == param.carrera_id ? 'selected' : ''}>
                                <c:out value="${carrera.titulo}" />
                            </option>
                        </c:forEach>
                            <option value="0" ${0==param.carrera_id ? 'selected' : ''}>Sin carrera específica</option>
                    </select>
                </form>
            </div>

            <!-- Listado de peticiones -->
            <div class="row">
                <c:choose>
                    <c:when test="${not empty peticiones}">
                        <c:forEach var="peticion" items="${peticiones}">
                            <div class="col-md-6 mb-3">
                                <div class="card shadow-sm">
                                    <div class="card-body">
                                        <h5 class="card-title text-primary">
                                            <c:out value="${peticion.getTitulo()}" />
                                        </h5>
                                        <p class="card-text">
                                            <c:out value="${fn:substring(peticion.getDescripcion(), 0, 50)}" />...
                                        </p>
                                        <p class="text-muted">Vencimiento: <fmt:formatDate value="${peticion.getVencimiento()}" pattern="dd/MM/yyyy" /></p>
                                        <a href="${pageContext.request.contextPath}/peticiones/votar?id=${peticion.getIdpeticion()}" class="stretched-link"></a>
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
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <div class="alert alert-secondary text-center" role="alert">
                                No hay peticiones
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            
        </div>
    </main>

    <!-- Footer -->
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
<script>
document.addEventListener('DOMContentLoaded', function () {
    const closeButtons = document.querySelectorAll('.btn-close');
    closeButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const alertBox = button.closest('.alert');
            alertBox.style.display = 'none';
        });
    });
});

</script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
