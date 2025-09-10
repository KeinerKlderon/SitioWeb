package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/prueba?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "2556229";

    /**
     * Método estático para obtener una conexión con la base de datos.
     * 
     * @return La conexión a la base de datos o null si ocurre un error.
     */
    public static Connection getConnection() {
        // Se declara la variable de conexión que inicialmente es null
        Connection conexion = null;
        try {
      
            // Cargar el driver JDBC de MySQL esto es necesario para que Java pueda interactuar con MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

 
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

        } catch (ClassNotFoundException e) {
         
            System.out.println("Error: Driver JDBC no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
           
            System.out.println("Error: No se pudo conectar a la base de datos.");
            e.printStackTrace();
        }
        // Finalmente, se retorna el objeto de conexión
        // Si hubo un error, la conexión será null
        return conexion;
    }
}
