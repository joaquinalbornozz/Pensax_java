<%-- 
    Document   : aprobar
    Created on : 13 nov 2024, 8:26:49
    Author     : users
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Detalles de la Petición</title>
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

<div class="container my-8 p-6 bg-white rounded-lg shadow-lg">
    <h2 class="font-weight-bold text-xl text-center mb-5">Detalles de la Petición</h2>

    <div class="bg-light p-4 rounded shadow-sm">
        <h2 class="font-weight-bold text-dark mb-4">${peticion.getTitulo()}</h2>
        <p class="text-secondary mb-4">${peticion.getDescripcion()}</p>
        <p class="text-muted mb-4">Carrera: ${carrera}</p>
        <p class="text-muted mb-4">Usuario: ${usuario}</p>
        <p class="text-muted mb-4">Vencimiento:<fmt:formatDate value="${peticion.getVencimiento()}" pattern="dd/MM/yyyy" /></p>

        <div class="d-flex mb-4">
            <form id="peticion-form" class="mr-3">
                <button type="button" class="btn btn-success" onclick="openModal('confirmarPublicacionModal')">
                    Aprobar y Publicar
                </button>
                <button type="button" class="btn btn-warning" onclick="openModal('confirmarRechazoModal')">
                    Rechazar
                </button>
                <button type="button" class="btn btn-danger" onclick="openModal('confirmarEliminacionModal')">
                    Eliminar
                </button>
            </form>
        </div>

        <c:if test="${not empty peticion.getImagen()}">
            <div class="ml-4">
                <c:choose>
                    <c:when test="${peticion.getImagen().startsWith('http')}">
                        <!-- Imagen de URL externa -->
                        <img src="${peticion.getImagen()}" 
                             alt="${peticion.getTitulo()}" 
                             class="rounded w-25 h-25 object-cover">
                    </c:when>
                    <c:otherwise>
                        <!-- Imagen almacenada localmente en el servidor -->
                        <img src="http://localhost:8080/Pensax/images?imageName=${peticion.getImagen()}" 
                             alt="${peticion.getTitulo()}" 
                             class="rounded w-25 h-25 object-cover">
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

    </div>
</div>

<!-- Modal for Publish Confirmation -->
<div class="modal fade" id="confirmarPublicacionModal" tabindex="-1" role="dialog" aria-labelledby="confirmarPublicacionLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="./publicar?id=${peticion.getIdpeticion()}">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmarPublicacionLabel">Confirmación de Publicación</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas publicar esta petición?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Sí, publicar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal for Reject Confirmation -->
<div class="modal fade" id="confirmarRechazoModal" tabindex="-1" role="dialog" aria-labelledby="confirmarRechazoLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="./rechazar?id=${peticion.getIdpeticion()}">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmarRechazoLabel">Confirmación de Rechazo</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas rechazar esta petición?</p>
                    <textarea name="comentario" placeholder="Comentario" class="form-control mt-3" required></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Sí, rechazar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal for Delete Confirmation -->
<div class="modal fade" id="confirmarEliminacionModal" tabindex="-1" role="dialog" aria-labelledby="confirmarEliminacionLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="./eliminar?id=${peticion.getIdpeticion()}">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmarEliminacionLabel">Confirmación de Eliminación</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas eliminar esta petición?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Sí, eliminar</button>
                </div>
            </form>
        </div>
    </div>
</div>
    <footer class="bg-dark text-white py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
<script>
    function openModal(modalId) {
        $("#" + modalId).modal('show');
    }
</script>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
