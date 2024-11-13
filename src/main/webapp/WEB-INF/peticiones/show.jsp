<%-- 
    Document   : show
    Created on : 12 nov 2024, 23:48:06
    Author     : users
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Petición</title>
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

    <div class="container mt-5">
        <div class="d-flex align-items-center mb-4">
            <h2 class="font-weight-bold text-gray-800">
                <c:choose>
                    <c:when test="${peticion.getPublicada()!=1}">
                        Petición
                    </c:when>
                    <c:otherwise>
                        Petición Vencida
                    </c:otherwise>
                </c:choose>
            </h2>
        </div>
        
        <div class="card shadow-sm p-4">
            <div class="card-body">
                <h2 class="text-3xl font-weight-bold text-dark mb-4">${peticion.getTitulo()}</h2>
                <p class="text-lg text-muted mb-4">${peticion.getDescripcion()}</p>
                <p class="text-sm text-muted mb-3">Vencimiento: <fmt:formatDate value="${peticion.getVencimiento()}" pattern="dd/MM/yyyy" /></p>

                <c:if test="${not empty peticion.getImagen()}">
                    <div class="text-center mb-4">
                        <img src="http://localhost:8080/Pensax/images?imageName=${peticion.getImagen()}" alt="${peticion.getTitulo()}" class="rounded w-70 h-70 object-cover img-fluid">
                    </div>
                </c:if>

                <p class="text-muted mb-3">Positivos: <span class="font-weight-bold">${peticion.getPositivos()}</span></p>
                <p class="text-muted mb-3">Negativos: <span class="font-weight-bold">${peticion.getNegativos()}</span></p>
                <p class="text-muted mb-3">Carrera: ${carrera}</p>
                <p class="text-muted mb-3">Usuario: ${usuario}</p>
                <p class="text-muted mb-3">Rechazada: <c:choose>
                    <c:when test="${peticion.getRechazada()==1}">Sí</c:when>
                    <c:otherwise>No</c:otherwise>
                </c:choose></p>
                <p class="text-muted mb-3">Comentario: ${peticion.getComentario()}</p>

                <c:if test="${not empty peticion.getVotoCollection()}">
                    <div class="mt-4">
                        <h3 class="text-2xl font-weight-semibold text-dark mb-3">Detalles de los votos</h3>
                        <ul class="list-group">
                            <c:forEach var="votante" items="${peticion.getVotoCollection()}">
                                <li class="list-group-item d-flex align-items-center">
                                    <div class="ml-3">
                                        <p class="font-weight-medium">
                                            <c:choose>
                                                <c:when test="${votante.getAnonimo()}">Anónimo</c:when>
                                                <c:otherwise>${votante.getUser().getApellido() + votante.getUser().getNombre()}</c:otherwise>
                                            </c:choose>
                                        </p>
                                        <p class="text-muted">
                                            votó <c:choose>
                                                <c:when test="${votante.getVoto()==1}">positivo</c:when>
                                                <c:otherwise>negativo</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
