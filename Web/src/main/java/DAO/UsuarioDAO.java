package DAO;

import Modelo.Usuario;
import Controlador.Conexion;

import java.sql.*;
import java.util.*;

public class UsuarioDAO {

	/**
	 * Agrega un nuevo usuario a la base de datos.
	 * 
	 * @param user El objeto Usuario con la información del usuario a agregar.
	 */
	public void addUser(Usuario user) {
		// SQL para insertar un nuevo usuario en la base de datos
		String sql = "INSERT INTO usuario(nombre, email, telefono, direccion) VALUES (?, ?, ?, ?)";
		try (Connection dbConnection = Conexion.getConnection(); // Establecemos la conexión a la base de datos
				PreparedStatement ps = dbConnection.prepareStatement(sql)) { // Preparamos la consulta SQL

			// Parámetros de la consulta
			ps.setString(1, user.getNombre());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getTelefono());
			ps.setString(4, user.getDireccion());

			// Ejecutamos la consulta para insertar el usuario
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Si ocurre un error, imprimimos el stack trace para depuración
		}
	}
	/**
	 * Elimina un usuario de la base de datos dado su ID.
	 * 
	 * @param userId El ID del usuario a eliminar.
	 */
	public void deleteUser(int userId) {
		String sql = "DELETE FROM usuario WHERE id=?";
		try (Connection dbConnection = Conexion.getConnection(); // Establecemos la conexión a la base de datos
				PreparedStatement ps = dbConnection.prepareStatement(sql)) { // Preparamos la consulta SQL

			ps.setInt(1, userId);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Si ocurre un error, imprimimos el stack trace para depuración
		}
	}

	/**
	 * Actualiza la información de un usuario en la base de datos.
	 * 
	 * @param user El objeto Usuario con los nuevos datos.
	 */
	public void updateUser(Usuario user) {
		String sql = "UPDATE usuario SET nombre=?, email=?, telefono=?, direccion=? WHERE id=?";
		try (Connection dbConnection = Conexion.getConnection(); // Establecemos la conexión a la base de datos
				PreparedStatement ps = dbConnection.prepareStatement(sql)) { // Preparamos la consulta SQL

			// Establecemos los valores de los parámetros de la consulta
			ps.setString(1, user.getNombre());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getTelefono());
			ps.setString(4, user.getDireccion());
			ps.setInt(5, user.getId()); // El ID es necesario para identificar qué usuario actualizar

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Si ocurre un error, imprimimos el stack trace para depuración
		}
	}

	/**
	 * Obtiene todos los usuarios registrados en la base de datos.
	 * 
	 * @return Una lista de objetos Usuario con los datos de todos los usuarios.
	 */
	public List<Usuario> getAllUsers() {
		List<Usuario> users = new ArrayList<>(); // Lista para almacenar todos los usuarios
		String sql = "SELECT * FROM usuario"; // SQL para obtener todos los usuarios
		try (Connection dbConnection = Conexion.getConnection(); // Establecemos la conexión a la base de datos
				PreparedStatement ps = dbConnection.prepareStatement(sql); // Preparamos la consulta SQL
				ResultSet rs = ps.executeQuery()) { // Ejecutamos la consulta y obtenemos el resultado

			// Iteramos sobre los resultados de la consulta
			while (rs.next()) {
				Usuario user = new Usuario(); // Creamos un nuevo objeto Usuario
				user.setId(rs.getInt("id"));
				user.setNombre(rs.getString("nombre"));
				user.setEmail(rs.getString("email"));
				user.setTelefono(rs.getString("telefono"));
				user.setDireccion(rs.getString("direccion"));

				users.add(user); // Agregamos el usuario a la lista de usuarios
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Si ocurre un error, imprimimos el stack trace para depuración
		}
		return users; // Devolvemos la lista de usuarios
	}

	/**
	 * Obtiene un usuario específico de la base de datos dado su ID.
	 * 
	 * @param userId El ID del usuario a obtener.
	 * @return Un objeto Usuario con los datos del usuario encontrado o null si no
	 *         existe.
	 */
	public Usuario getUserById(int userId) {
		Usuario user = null; // Inicializamos el usuario como null
		String sql = "SELECT * FROM usuario WHERE id=?"; // SQL para obtener un usuario por su ID

		try (Connection dbConnection = Conexion.getConnection(); 
				PreparedStatement ps = dbConnection.prepareStatement(sql)) { 

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) { // Ejecutamos la consulta y obtenemos el resultado
				// Si encontramos un usuario con el ID proporcionado
				if (rs.next()) {
					user = new Usuario(); 
					user.setId(rs.getInt("id"));
					user.setNombre(rs.getString("nombre"));
					user.setEmail(rs.getString("email"));
					user.setTelefono(rs.getString("telefono"));
					user.setDireccion(rs.getString("direccion"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Si ocurre un error, imprimimos el stack trace para depuración
		}
		return user; // Devolvemos el usuario encontrado o null si no se encontró
	}
}
