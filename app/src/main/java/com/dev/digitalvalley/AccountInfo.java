package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AccountInfo extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView userName, phoneNumber, joinDate;

    TextView signOut;
    ImageView backButton, qrcodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        firebaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.accountinfo_name);
        phoneNumber = findViewById(R.id.accountinfo_phonenumber);
        joinDate = findViewById(R.id.accountinfo_joindate);
        signOut = findViewById(R.id.accountinfo_signout);
        backButton = findViewById(R.id.accountinfo_back);
        qrcodeImageView = findViewById(R.id.accountinfo_qrcode);

        DocumentReference accountReference = FirebaseFirestore.getInstance().document(String.format("Accounts/%s", firebaseAuth.getUid()));

        accountReference.get().addOnSuccessListener(documentSnapshot -> {
            String fullName = documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName");
            userName.setText(fullName);
            String phoneNumberWithCode = "+91 " + documentSnapshot.getString("PhoneNumber");
            phoneNumber.setText(phoneNumberWithCode);
            Date date = new Date(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getMetadata()).getCreationTimestamp());
            Format format = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String joinDateFinal = "Joined: " + format.format(date);
            joinDate.setText(joinDateFinal);
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix matrix = writer.encode(documentSnapshot.getString("PhoneNumber"), BarcodeFormat.QR_CODE,500, 500);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                qrcodeImageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
        });

        signOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(AccountInfo.this, Register.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }
}