<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enviar Correo</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .form-container {
            background-color: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 400px;
            margin: 0 auto;
        }
        .form-container h2 {
            text-align: center;
            color: #333;
        }
        label {
            font-weight: bold;
        }
        input[type="text"], input[type="email"], textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 12px 20px;
            cursor: pointer;
            border-radius: 4px;
            width: 100%;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .message {
            text-align: center;
            margin-top: 20px;
        }
        .message a {
            color: #333;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Formulario para Enviar Correo</h2>
        <form action="EnviarC" method="POST">
            <label for="destinatario">Destinatario:</label>
            <input type="email" id="destinatario" name="destinatario" required>

            <label for="asunto">Asunto:</label>
            <input type="text" id="asunto" name="asunto" required>

            <label for="mensaje">Mensaje:</label>
            <textarea id="mensaje" name="mensaje" rows="4" required></textarea>

            <input type="submit" value="Enviar Correo">
        </form>
    </div>

    <%-- Mostrar mensaje si se envió correctamente o hubo un error --%>
    <div class="message">
        <%
            String status = request.getParameter("status");
            if (status != null) {
                if (status.equals("success")) {
        %>
            <h3 style="color: green;">¡Correo enviado exitosamente!</h3>
            <a href="EnviarCorreo.jsp">Volver al formulario</a>
        <%
                } else if (status.equals("error")) {
        %>
            <h3 style="color: red;">Hubo un error al enviar el correo. Intenta nuevamente.</h3>
            <a href="EnviarCorreo.jsp">Volver al formulario</a>
        <%
                }
            }
        %>
    </div>
</body>
</html>
