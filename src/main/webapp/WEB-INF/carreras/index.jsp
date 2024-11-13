<%-- 
    Document   : index
    Created on : 13 nov 2024, 11:56:56
    Author     : users
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Carreras</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
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
<div class="container my-8 p-6 bg-white rounded-lg shadow-lg">
    <!-- Header -->
    <div class="text-center mb-5">
        <h2 class="font-weight-bold text-dark">
            Carreras
        </h2>
    </div>

    <!-- Mensaje de éxito -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success font-weight-bold mb-4">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <!-- Error Message -->
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger font-weight-bold mb-4">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Botón para crear nueva carrera -->
    <div class="d-flex justify-content-start mb-4">
        <a href="${pageContext.request.contextPath}/carreras/create" class="btn btn-secondary">
            Nueva Carrera
        </a>
    </div>

    <!-- Listado de carreras -->
    <div class="row">
        <c:forEach items="${carreras}" var="carrera">
            <div class="col-md-4 mb-4">
                <div class="card bg-light shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">
                            <a href="${pageContext.request.contextPath}/carreras/editar?id=${carrera.getIdCarrera()}" class="text-dark">
                                ${carrera.titulo}
                            </a>
                        </h5>
                        <button type="button" class="btn btn-link text-danger p-0 float-right" data-toggle="modal" data-target="#confirmarEliminacion${carrera.getIdCarrera()}">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" width="20" height="20">
                                <path stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                            </svg>
                        </button>
                    </div>
                </div>
            </div>

            <!-- Modal de confirmación de eliminación -->
            <div class="modal fade" id="confirmarEliminacion${carrera.getIdCarrera()}" tabindex="-1" aria-labelledby="confirmarEliminacionLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Confirmar Eliminación</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <form method="post" action="${pageContext.request.contextPath}/carreras/eliminar?id=${carrera.getIdCarrera()}">
                            <div class="modal-body">
                                <p>¿Estás seguro de que deseas eliminar esta carrera?</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-danger">Sí, eliminar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>

        <!-- Mensaje si no hay carreras -->
        <c:if test="${carreras.isEmpty()}">
            <div class="col-12">
                <div class="alert alert-warning text-center">
                    No hay carreras
                </div>
            </div>
        </c:if>
    </div>
    
</div>
    <footer class="bg-dark text-white  py-4 mt-auto">
        <div class="container text-center">
            <jsp:useBean id="now" class="java.util.Date" scope="request" />
            <p>&copy; <fmt:formatDate value="${now}" pattern="yyyy" /> Pensax. All rights reserved.</p>
        </div>
    </footer>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>