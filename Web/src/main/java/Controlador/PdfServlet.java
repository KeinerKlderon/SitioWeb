package Controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet("/PdfServlet") // Indica que este servlet se invoca con la URL "/PdfServlet"
public class PdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PdfServlet() {
		super();
	}

	/**
	 * Método que maneja la solicitud GET. Genera un archivo PDF con la lista de
	 * usuarios de la base de datos.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Se establece el tipo de contenido de la respuesta como PDF para que se descargue o visualice como un archivo PDF
		response.setContentType("application/pdf");

		// Datos de conexión a la base de datos
		final String URL = "jdbc:mysql://localhost:3306/prueba?useSSL=false&serverTimezone=UTC";
		final String USER = "root";
		final String PASSWORD = "2556229";

		Connection connection = null;

		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);

			Document document = new Document();

			// Enlazar el documento con la salida de la respuesta para enviar el PDF
			PdfWriter.getInstance(document, response.getOutputStream());

			document.open();

			// Crear una sentencia SQL para obtener los usuarios de la base de datos
			Statement stmt = connection.createStatement();

			// Ejecutar la consulta SQL para obtener el nombre, email, teléfono y dirección
			// de los usuarios
			ResultSet rs = stmt.executeQuery("SELECT nombre, email, telefono, direccion FROM usuario");

			// Agregar un título inicial al documento PDF
			document.add(new Paragraph("Lista de Usuarios:\n\n"));

			// Recorrer todos los resultados obtenidos de la base de datos
			while (rs.next()) {

				String nombre = rs.getString("nombre");
				String email = rs.getString("email");
				String telefono = rs.getString("telefono");
				String direccion = rs.getString("direccion");

				// Formatear la información del usuario en una línea de texto
				String linea = String.format("Nombre: %s | Email: %s | Teléfono: %s | Dirección: %s", nombre, email,
						telefono, direccion);

				// Agregar esta línea de texto al documento PDF
				document.add(new Paragraph(linea));
			}

			document.close();

			// Cerrar los recursos utilizados para la conexión y consulta a la base de datos
			rs.close();
			stmt.close();
			connection.close();

		} catch (Exception e) {
			throw new ServletException("Error al generar el PDF", e);
		}
	}
}
