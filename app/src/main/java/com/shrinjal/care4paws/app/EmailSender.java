package com.shrinjal.care4paws.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailSender {

    private static final String EMAIL = "sonishrinjal0@gmail.com";
    private static final String PASSWORD = "ujgihscjasvdkrjm";
    private static final String RECEIVERS =
            "sonishrinjal0@gmail.com,ajit.789singh@gmail.com";

    public static void send(
            Context context,
            Uri imageUri,
            double latitude,
            double longitude,
            String userName,
            String animalType,
            String description
    ) {

        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(
                        props,
                        new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(EMAIL, PASSWORD);
                            }
                        }
                );

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(RECEIVERS)
                );
                message.setSubject("🐾 Care4Paws – Injured Animal Report");

                // TEXT BODY
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(
                        "Reporter: " + userName +
                                "\nAnimal: " + animalType +
                                "\nDescription: " + description +
                                "\nLatitude: " + latitude +
                                "\nLongitude: " + longitude
                );

                // IMAGE PART (SAFE WAY)
                InputStream is =
                        context.getContentResolver().openInputStream(imageUri);

                ByteArrayDataSource dataSource =
                        new ByteArrayDataSource(is, "image/jpeg");

                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setDataHandler(new DataHandler(dataSource));
                imagePart.setFileName("animal_report.jpg");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(imagePart);

                message.setContent(multipart);

                Transport.send(message);

                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(
                                context,
                                "Report sent successfully 📧",
                                Toast.LENGTH_LONG
                        ).show()
                );

            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(
                                context,
                                "Email failed: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
            }
        }).start();
    }
}
