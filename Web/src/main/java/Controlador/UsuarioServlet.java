package Controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import DAO.UsuarioDAO;
import Modelo.Usuario;

@WebServlet("/UsuarioServlet") // Indica que este servlet se invoca con la URL "/UsuarioServlet"
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Definimos las vistas a las que redirigirá el servlet
    private static final String INSERT_OR_EDIT = "/vista/formularioUsuario.jsp"; // Vista para agregar/editar usuario
    private static final String LIST_USER = "/vista/listaUsuarios.jsp"; // Vista para listar usuarios

    // Instancia de la clase DAO que maneja las operaciones con la base de datos
    private UsuarioDAO dao = new UsuarioDAO();

    /**
     * Maneja las solicitudes GET. Dependiendo de la acción (delete, edit, insert, list), 
     * redirige a la vista correspondiente.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se obtiene el parámetro 'action' que indica la operación a realizar
        String action = request.getParameter("action");
        String forward;

        // Si la acción es "delete", eliminamos el usuario
        if ("delete".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteUser(id); 
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "ID de usuario inválido.");
            }
            // Después de eliminar, redirigimos a la lista de usuarios
            request.setAttribute("users", dao.getAllUsers());
            forward = LIST_USER;

        } else if ("edit".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Usuario user = dao.getUserById(id);
                request.setAttribute("user", user);
                forward = INSERT_OR_EDIT;
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "ID de usuario inválido.");
                request.setAttribute("users", dao.getAllUsers());
                forward = LIST_USER;
            }

        } else if ("insert".equalsIgnoreCase(action)) {
            Usuario user = new Usuario();
            request.setAttribute("user", user);
            forward = INSERT_OR_EDIT;

        } else {
            request.setAttribute("users", dao.getAllUsers());
            forward = LIST_USER;
        }

        // Redirigimos a la vista correspondiente
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        dispatcher.forward(request, response);
    }

    /**
     * Maneja las solicitudes POST. Se utiliza para agregar o editar un usuario.
     * Dependiendo de si el ID está vacío o no, determina si es un nuevo usuario o uno existente.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Creamos un nuevo objeto Usuario con los datos del formulario
        Usuario user = new Usuario();
        user.setNombre(request.getParameter("nombre"));
        user.setEmail(request.getParameter("email"));
        user.setTelefono(request.getParameter("telefono"));
        user.setDireccion(request.getParameter("direccion"));

        // Obtenemos el ID del usuario desde el formulario
        String id = request.getParameter("id");

        // Verificamos si el ID es nulo o vacío, lo que indica que es un nuevo usuario
        boolean esNuevo = (id == null || id.isEmpty() || id.equals("0"));

        // Si es un nuevo usuario, lo agregamos a la base de datos
        if (esNuevo) {
            dao.addUser(user);
            request.setAttribute("mensaje", "Usuario registrado correctamente.");
        } else {
            // Si no es nuevo, lo actualizamos en la base de datos
            user.setId(Integer.parseInt(id));
            dao.updateUser(user);
            request.setAttribute("mensaje", "Usuario actualizado correctamente.");
        }

        // Recuperamos la lista de todos los usuarios y la mostramos
        request.setAttribute("users", dao.getAllUsers());
        // Redirigimos a la vista de lista de usuarios
        RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_USER);
        dispatcher.forward(request, response);
    }
}
