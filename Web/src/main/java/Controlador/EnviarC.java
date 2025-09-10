package Controlador;


import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;



/**
 * Servlet implementation class EnviarCorreoServlet
 */
@WebServlet("/EnviarC")
public class EnviarC extends HttpServlet {
        private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnviarC() {
        super();
        // TODO Auto-generated constructor stub
    }

        /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // TODO Auto-generated method stub
                response.getWriter().append("Served at: ").append(request.getContextPath());
        }

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // TODO Auto-generated method stub
                doGet(request, response);

        // Leer datos del formulario correctamente
        String destinatario = request.getParameter("destinatario");
        String asunto = request.getParameter("asunto");
        String mensajeTexto = request.getParameter("mensaje");

        //  Tu cuenta de Gmail (remitente)
        String remitente = "caligomez294@gmail.com";
        String clave = "cqec dtwz unny xiia"; // Contraseña de aplicación, no la personal

        //  Configuración SMTP //Protocolo de transferencia de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crea una sesión auténticada
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            // Crear mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensajeTexto);

            //  Enviar
            Transport.send(message);

            // Mostrar mensaje en respuesta
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><body style='font-family:sans-serif; text-align:center; padding-top:40px;'>");
            out.println("<h2 style='color:green;'>¡Correo enviado exitosamente a " + destinatario + "!</h2>");
            out.println("<a href='EnviarCorreo.jsp'>← Volver</a>");
            out.println("</body></html>");

        } catch (MessagingException e) {
            //  Mostrar error
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><body style='font-family:sans-serif; text-align:center; padding-top:40px;'>");
            out.println("<h2 style='color:red;'>Error al enviar correo:</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='EnviarCorreo.jsp'>← Volver</a>");
            out.println("</body></html>");
        }
    }
}