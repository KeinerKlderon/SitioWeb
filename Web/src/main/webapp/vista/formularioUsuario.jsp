<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Modelo.Usuario"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulario de Usuario</title>
</head>
<body>

    <%
        // Si no existe, creamos un nuevo objeto Usuario vacío
        Usuario user = (Usuario) request.getAttribute("user");
        if (user == null) {
            user = new Usuario();  // Se asigna un nuevo usuario si no se pasó uno
        }
    %>

    <h2><%=(user.getId() == 0) ? "Agregar Usuario" : "Editar Usuario"%></h2>

    <!-- Formulario que enviará los datos del usuario al servlet 'UsuarioServlet' -->
    <form action="UsuarioServlet" method="post">
        <input type="hidden" name="id" value="<%=(user.getId() == 0) ? "" : user.getId()%>" />

        Nombre: <input type="text" name="nombre" value="<%=user.getNombre()%>" required /> <br />

        Email: <input type="email" name="email" value="<%=user.getEmail()%>" required /> <br />

        Teléfono: <input type="text" name="telefono" value="<%=user.getTelefono()%>" required /> <br />

        Dirección: <input type="text" name="direccion" value="<%=user.getDireccion()%>" required /> <br /><br />

        <input type="submit" value="Guardar" />
    </form>

    <br />
    <a href="UsuarioServlet?action=listUser">Volver a la lista</a>

</body>
</html>
