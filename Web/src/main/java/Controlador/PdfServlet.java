
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


@WebServlet("/PdfServlet")
public class PdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    
    public PdfServlet() {
        super();
    }

    // lee la accion 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se establece contenido de la respuesta como PDF
        response.setContentType("application/pdf");

        // Datos de conexión a la base de datos
        final String URL = "jdbc:mysql://localhost:3306/prueba?useSSL=false&serverTimezone=UTC";
        final String USER = "root";        
        final String PASSWORD = "2556229";  

        Connection connection = null;

        try {
            // Se carga el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Se establece la conexión con la base de datos
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Se crea un nuevo documento PDF
            Document document = new Document();

            // Se enlaza el documento PDF con la salida del response (para que se abra directamente)
            PdfWriter.getInstance(document, response.getOutputStream());

            // Se abre el documento para comenzar a escribir en él
            document.open();

            // Se crea una sentencia SQL
            Statement stmt = connection.createStatement();

            // Se ejecuta la consulta SQL para obtener los datos de los usuarios
            ResultSet rs = stmt.executeQuery("SELECT nombre, email, telefono, direccion FROM usuario");

            // Se agrega un título inicial al documento
            document.add(new Paragraph("Lista de Usuarios:\n\n"));

            // Se recorren todos los registros del resultado de la consulta
            while (rs.next()) {
                // Se obtienen los campos del usuario
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                String direccion = rs.getString("direccion");

                // Se formatea la información en una línea de texto
                String linea = String.format("Nombre: %s | Email: %s | Teléfono: %s | Dirección: %s", 
                        nombre, email, telefono, direccion);

                // Se agrega la línea al documento PDF
                document.add(new Paragraph(linea));
            }

            // Se cierra el documento una vez terminado
            document.close();

            // Se cierran recursos de base de datos
            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            // Si ocurre un error, se lanza una excepción de servlet con el mensaje de error
            throw new ServletException("Error al generar el PDF", e);
        }
    }
}
