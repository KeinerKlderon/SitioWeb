package DAO;

import Modelo.Usuario;
import Controlador.Conexion;

import java.sql.*;
import java.util.*;

public class UsuarioDAO {

    public void addUser(Usuario user) {
        String sql = "INSERT INTO usuario(nombre, email, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (Connection dbConnection = Conexion.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getDireccion());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM usuario WHERE id=?";
        try (Connection dbConnection = Conexion.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Usuario user) {
        String sql = "UPDATE usuario SET nombre=?, email=?, telefono=?, direccion=? WHERE id=?";
        try (Connection dbConnection = Conexion.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getDireccion());
            ps.setInt(5, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> getAllUsers() {
        List<Usuario> users = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection dbConnection = Conexion.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setEmail(rs.getString("email"));
                user.setTelefono(rs.getString("telefono"));
                user.setDireccion(rs.getString("direccion"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Usuario getUserById(int userId) {
        Usuario user = null;
        String sql = "SELECT * FROM usuario WHERE id=?";

        try (Connection dbConnection = Conexion.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
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
            e.printStackTrace();
        }
        return user;
    }
}
