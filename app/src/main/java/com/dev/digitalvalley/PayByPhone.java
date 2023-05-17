package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PayByPhone extends AppCompatActivity {
    private CollectionReference accountReference;
    AppCompatImageView backButton;
    TextView errorMessage;
    EditText phoneNumberEditText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_phone);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        accountReference = FirebaseFirestore.getInstance().collection("Accounts");
        phoneNumberEditText = findViewById(R.id.paybyphone_phonenumber);
        progressBar = findViewById(R.id.paybyphone_progress_bar);
        errorMessage = findViewById(R.id.paybyphone_error_msg);
        phoneNumberEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if ((!phoneNumberEditText.toString().isEmpty()) && phoneNumberEditText.getText().toString().trim().length() == 10) {
                    checkPhoneNumber(phoneNumberEditText.getText().toString().trim());
                    errorMessage.setText(R.string.blank);
                }
                return true;
            }
            return false;
        });
        backButton = findViewById(R.id.paybyphone_back);
        backButton.setOnClickListener(v -> finish());
    }

    private void checkPhoneNumber(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        accountReference.whereEqualTo("PhoneNumber", phoneNumber).get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) {
                errorMessage.setText(String.format("No match found for +91 %s", phoneNumber));
            } else {
                String receiver = task.getResult().getDocuments().get(0).getId();
                String fullName = task.getResult().getDocuments().get(0).getString("FirstName") + " " + task.getResult().getDocuments().get(0).getString("LastName");
                Intent intent = new Intent(PayByPhone.this, SetAmount.class);
                intent.putExtra("phoneNumber", String.format("+91 %s", phoneNumber));
                intent.putExtra("receiverId", receiver);
                intent.putExtra("fullName", fullName);
                startActivity(intent);
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}