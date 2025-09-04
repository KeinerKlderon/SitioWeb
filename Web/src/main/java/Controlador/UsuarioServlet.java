package Controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import DAO.UsuarioDAO;
import Modelo.Usuario;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.*;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String INSERT_OR_EDIT = "/vista/formularioUsuario.jsp";
    private static final String LIST_USER = "/vista/listaUsuarios.jsp";

    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String forward;

        if ("delete".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteUser(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "ID de usuario inválido.");
            }
            request.setAttribute("users", dao.getAllUsers());
            forward = LIST_USER;

        } else if ("edit".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Usuario user = dao.getUserById(id);
                request.setAttribute("user", user);
                forward = INSERT_OR_EDIT;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "ID de usuario inválido.");
                request.setAttribute("users", dao.getAllUsers());
                forward = LIST_USER;
            }

        } else if ("insert".equalsIgnoreCase(action)) {
            Usuario user = new Usuario();
            request.setAttribute("user", user);
            forward = INSERT_OR_EDIT;

        } else if ("buscarPorId".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Usuario user = dao.getUserById(id);
                if (user != null) {
                    request.setAttribute("user", user);
                    forward = INSERT_OR_EDIT;
                } else {
                    request.setAttribute("mensaje", "Usuario no encontrado.");
                    request.setAttribute("users", dao.getAllUsers());
                    forward = LIST_USER;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "ID de usuario inválido.");
                request.setAttribute("users", dao.getAllUsers());
                forward = LIST_USER;
            }

        } else {
            request.setAttribute("users", dao.getAllUsers());
            forward = LIST_USER;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario user = new Usuario();
        user.setNombre(request.getParameter("nombre"));
        user.setEmail(request.getParameter("email"));
        user.setTelefono(request.getParameter("telefono"));
        user.setDireccion(request.getParameter("direccion"));

        String id = request.getParameter("id");
        boolean esNuevo = (id == null || id.isEmpty() || id.equals("0"));

        if (esNuevo) {
            dao.addUser(user);

            // Enviar correo con PDF
            try {
                enviarCorreoPDF();
                request.setAttribute("mensaje", "Usuario agregado y correo enviado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "Usuario agregado, pero error al enviar el correo.");
            }

        } else {
            try {
                user.setId(Integer.parseInt(id));
                dao.updateUser(user);
                request.setAttribute("mensaje", "Usuario actualizado correctamente.");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "ID de usuario inválido.");
            }
        }

        request.setAttribute("users", dao.getAllUsers());
        RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_USER);
        dispatcher.forward(request, response);
    }

    private void enviarCorreoPDF() throws Exception {
        final String remitente = "keineroviedo03@gmail.com";
        final String clave = "djhf pxpr vxsy mjso"; // Cambia por tu password de app de Gmail
        final String destinatario = "wendyvizcaino73@gmail.com";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        List<Usuario> users = dao.getAllUsers();

        document.add(new Paragraph("Lista de Usuarios"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Nombre");
        table.addCell("Email");
        table.addCell("Teléfono");
        table.addCell("Dirección");

        for (Usuario user : users) {
            table.addCell(user.getNombre());
            table.addCell(user.getEmail());
            table.addCell(user.getTelefono());
            table.addCell(user.getDireccion());
        }

        document.add(table);
        document.close();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remitente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Nuevo usuario registrado - PDF actualizado");

        MimeBodyPart texto = new MimeBodyPart();
        texto.setText("Hola, se ha registrado un nuevo usuario. Adjunto el PDF actualizado con todos los usuarios.");

        MimeBodyPart adjunto = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        adjunto.setDataHandler(new DataHandler(source));
        adjunto.setFileName("usuarios.pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(adjunto);

        message.setContent(multipart);

        Transport.send(message);
    }
}
