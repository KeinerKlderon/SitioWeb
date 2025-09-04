package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private static final String URL = "jdbc:mysql://localhost:3306/prueba?useSSL=false&serverTimezone=UTC";
	private static final String USUARIO = "root";
	private static final String CONTRASENA = "2556229";

	public static Connection getConnection() {
		Connection conexion = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: Driver JDBC no encontrado.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error: No se pudo conectar a la base de datos.");
			e.printStackTrace();
		}
		return conexion;
	}
}
