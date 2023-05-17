package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScanQR extends AppCompatActivity {
    private CollectionReference accountReference;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        accountReference = FirebaseFirestore.getInstance().collection("Accounts");
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            checkPhoneNumber(result.getText());
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void checkPhoneNumber(String phoneNumber) {
        accountReference.whereEqualTo("PhoneNumber", phoneNumber).get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) {
                Toast.makeText(this, String.format("No match found for +91 %s", phoneNumber), Toast.LENGTH_SHORT).show();
            } else {
                String receiver = task.getResult().getDocuments().get(0).getId();
                String fullName = task.getResult().getDocuments().get(0).getString("FirstName") + " " + task.getResult().getDocuments().get(0).getString("LastName");
                Intent intent = new Intent(ScanQR.this, SetAmount.class);
                intent.putExtra("phoneNumber", String.format("+91 %s", phoneNumber));
                intent.putExtra("receiverId", receiver);
                intent.putExtra("fullName", fullName);
                startActivity(intent);
            }
        });
    }
}