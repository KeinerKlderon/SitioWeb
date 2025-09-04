<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, Modelo.Usuario"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lista de Usuarios</title>
</head>
<body>

    <h2>Usuarios Registrados</h2>

    <%
    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje != null) {
    %>
        <p style="color: red;"><%= mensaje %></p>
    <%
    }
    %>

    <%
    List<Usuario> users = (List<Usuario>) request.getAttribute("users");
    if (users != null && !users.isEmpty()) {
    %>

    <table border="1" cellpadding="5" cellspacing="0">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
                <th>Teléfono</th>
                <th>Dirección</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <%
        for (Usuario user : users) {
        %>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getNombre() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getTelefono() %></td>
            <td><%= user.getDireccion() %></td>
            <td>
                <a href="UsuarioServlet?action=edit&id=<%= user.getId() %>">Editar</a> |
                <a href="UsuarioServlet?action=delete&id=<%= user.getId() %>" onclick="return confirm('¿Eliminar este usuario?');">Eliminar</a>
            </td>
        </tr>
        <%
        }
        %>
        </tbody>
    </table>

    <%
    } else {
    %>
    <p>No hay usuarios registrados.</p>
    <%
    }
    %>

    <br>
    <a href="UsuarioServlet?action=insert">Agregar nuevo usuario</a>

    <br><br>
    <form action="PdfServlet" method="get" target="_blank">
        <button type="submit">Descargar PDF</button>
    </form>

</body>
</html>
