<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Pensax</title>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.bunny.net">
    <link href="https://fonts.bunny.net/css?family=figtree:400,600&display=swap" rel="stylesheet" />

    <!-- Styles -->
    <style>
        /* Aquí irían los estilos similares a Tailwind CSS */
        body {
            font-family: 'Figtree', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f3f4f6;
        }
        .container {
            text-align: center;
            padding: 2rem;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 34px rgba(0, 0, 0, 0.06);
            max-width: 400px;
            width: 100%;
        }
        h1 {
            font-size: 1.875rem;
            color: #000;
        }
        .button {
            display: inline-block;
            width: 100%;
            padding: 0.75rem;
            margin-top: 1rem;
            font-size: 1rem;
            color: #fff;
            text-align: center;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }
        .button-login {
            background-color: #f97316; /* Color naranja para Iniciar Sesión */
        }
        .button-register {
            background-color: #2563eb; /* Color azul para Registrarse */
        }
        .button-home {
            background-color: #10b981; /* Color verde para Home */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenido a la Aplicación</h1>
        <p>Seleccione una opción para continuar</p>

        <%
            // Obtener la sesión actual
            HttpSession session1 = request.getSession(false);
            
            // Verificar si el usuario tiene una sesión activa
            if (session1 != null && session1.getAttribute("user") != null) {
        %>
            <!-- Botón para ir a Home si la sesión está activa -->
            <a href="./peticiones.jsp" class="button button-home">Home</a>
        <%
            } else {
        %>
            <!-- Botones para Iniciar Sesión y Registrarse si no hay sesión activa -->
            <a href="./login" class="button button-login">Iniciar Sesión</a>
            <a href="./registrar" class="button button-register">Registrarse</a>
        <%
            }
        %>
    </div>
</body>
</html>
