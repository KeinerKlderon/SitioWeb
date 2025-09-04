package Controlador;

import Modelo.Usuario;
import DAO.UsuarioDAO;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class CorreoUtils {

    // Método para enviar correo con PDF, recibe destinatario, remitente y clave
    public static void enviarCorreoConPDF(String remitente, String clave, String destinatario) throws Exception {
        // Generar PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        List<Usuario> users = new UsuarioDAO().getAllUsers();

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

        // Configurar propiedades SMTP
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
        texto.setText("Hola, se ha registrado un nuevo usuario. Adjunto el PDF actualizado.");

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
