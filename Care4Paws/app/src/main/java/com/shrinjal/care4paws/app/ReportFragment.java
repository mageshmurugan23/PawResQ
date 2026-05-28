package com.shrinjal.care4paws.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReportFragment extends Fragment {

    private static final int REQ_CAMERA = 101;
    private static final int REQ_GALLERY = 102;
    private static final int REQ_LOCATION = 201;

    EditText animalType, description;
    ImageView imgPreview;
    Button btnCamera, btnGallery, btnGetLocation, submitBtn;
    TextView txtLatitude, txtLongitude;

    File imageFile;
    Uri imageUri;

    double latitude = 0.0, longitude = 0.0;
    FusedLocationProviderClient locationClient;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        animalType = view.findViewById(R.id.animalType);
        description = view.findViewById(R.id.description);
        imgPreview = view.findViewById(R.id.imgPreview);

        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnGetLocation = view.findViewById(R.id.btnGetLocation);
        submitBtn = view.findViewById(R.id.submitBtn);

        txtLatitude = view.findViewById(R.id.txtLatitude);
        txtLongitude = view.findViewById(R.id.txtLongitude);

        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        btnGetLocation.setOnClickListener(v -> fetchLocation());
        submitBtn.setOnClickListener(v -> validateAndSubmit());

        return view;
    }

    // ================= VALIDATION =================
    private void validateAndSubmit() {

        if (animalType.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Enter animal type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageFile == null) {
            Toast.makeText(getContext(), "Attach image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitude == 0.0 || longitude == 0.0) {
            Toast.makeText(getContext(), "Fetch location first", Toast.LENGTH_SHORT).show();
            return;
        }

        saveToRoom();
        sendEmailInBackground();

        Toast.makeText(getContext(), "Report Submitted Successfully", Toast.LENGTH_LONG).show();
    }

    // ================= LOCATION =================
    private void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOCATION
            );
            return;
        }

        locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
        ).addOnSuccessListener(location -> {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                txtLatitude.setText("Latitude: " + latitude);
                txtLongitude.setText("Longitude: " + longitude);

                txtLatitude.setVisibility(View.VISIBLE);
                txtLongitude.setVisibility(View.VISIBLE);
            }
        });
    }

    // ================= SAVE TO ROOM =================
    private void saveToRoom() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        new Thread(() -> {

            ReportEntity report = new ReportEntity();

            report.userId = user.getUid();
            report.userName = user.getEmail(); // ✅ FIX

            report.animalType = animalType.getText().toString();
            report.imagePath = imageFile.getAbsolutePath();
            report.latitude = latitude;
            report.longitude = longitude;
            report.status = "Pending";

            AppDatabase.getInstance(requireContext())
                    .reportDao()
                    .insert(report);

        }).start();
    }

    // ================= AUTO EMAIL =================
    private void sendEmailInBackground() {

        new Thread(() -> {
            try {

                final String senderEmail = "sonishrinjal0@gmail.com";
                final String senderPassword = "cfkszyvngfqmddmk";

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(senderEmail, senderPassword);
                            }
                        });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(senderEmail));

                message.setSubject("New Animal Report - Care4Paws");

                message.setText(
                        "Animal Type: " + animalType.getText().toString() + "\n\n" +
                                "Description: " + description.getText().toString() + "\n\n" +
                                "Latitude: " + latitude + "\n" +
                                "Longitude: " + longitude + "\n\n" +
                                "Status: Pending"
                );

                Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ================= CAMERA =================
    private void openCamera() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    REQ_CAMERA
            );
            return;
        }

        try {
            imageFile = File.createTempFile(
                    "animal_",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    imageFile
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQ_CAMERA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GALLERY =================
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_GALLERY);
    }

    // ================= RESULT =================
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQ_CAMERA) {
                imgPreview.setImageURI(imageUri);
            }

            if (requestCode == REQ_GALLERY && data != null) {
                imageUri = data.getData();
                imgPreview.setImageURI(imageUri);
                imageFile = copyUriToFile(imageUri);
            }
        }
    }

    // ================= COPY FILE =================
    private File copyUriToFile(Uri uri) {

        try {
            InputStream input =
                    requireContext().getContentResolver().openInputStream(uri);

            File file = File.createTempFile(
                    "gallery_",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }

            output.close();
            input.close();

            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}