<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, Modelo.Usuario"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lista de Usuarios</title>

<script>
        // Esta función se ejecuta cuando la página se carga
        window.onload = function () {
            // Obtener el mensaje de error, si existe, desde el atributo 'mensaje' en la solicitud
            const mensaje = "<%=(request.getAttribute("mensaje") != null)
		? request.getAttribute("mensaje").toString().replace("\"", "\\\"")
		: ""%>
	";

		// Si el mensaje no está vacío, mostrar una alerta con el mensaje de error
		if (mensaje.trim() !== "") {
			alert(mensaje);
		}
	};
</script>
</head>
<body>

	<h2>Usuarios Registrados</h2>

	<%
	// Obtener la lista de usuarios desde la solicitud 
	List<Usuario> users = (List<Usuario>) request.getAttribute("users");

	// Verificar si la lista de usuarios no es nula ni vacía
	if (users != null && !users.isEmpty()) {
	%>


	<table border="1" cellpadding="5" cellspacing="0">
		<thead>
			<tr>
				<!-- Encabezados de la tabla -->
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
			// Recorrer la lista de usuarios y generar una fila de tabla para cada usuario
			for (Usuario user : users) {
			%>
			<tr>
				<!-- Mostrar los datos de cada usuario en las celdas correspondientes -->
				<td><%=user.getId()%></td>
				<td><%=user.getNombre()%></td>
				<td><%=user.getEmail()%></td>
				<td><%=user.getTelefono()%></td>
				<td><%=user.getDireccion()%></td>
				<td>
					<!-- Enlaces para editar o eliminar el usuario --> <a
					href="UsuarioServlet?action=edit&id=<%=user.getId()%>">Editar</a>
					| <a href="UsuarioServlet?action=delete&id=<%=user.getId()%>"
					onclick="return confirm('¿Eliminar este usuario?');">Eliminar</a>
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
	<br>

	<form action="vista/EnviarCorreo.jsp" method="POST">
		<!-- Botón para enviar correo -->
		<button type="submit">Enviar Correo</button>
	</form>
	<br>
	<br>
	<form action="PdfServlet" method="get" target="_blank">
		<!-- Botón para generar y descargar el PDF -->
		<button type="submit">Descargar PDF</button>
	</form>

</body>
</html>
