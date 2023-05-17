package com.dev.digitalvalley;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class Register extends AppCompatActivity {
    private FirebaseAuth firebaseAuthentication;
    EditText editTextEmail;
    Button confirmButton;
    TextView registerErrorMsg;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        firebaseAuthentication = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.register_email);
        confirmButton = findViewById(R.id.register_confirm_button);
        registerErrorMsg = findViewById(R.id.register_error_msg);
        progressBar = findViewById(R.id.register_progress_bar);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmButton.setEnabled(!s.toString().isEmpty() && isEmailValid(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmButton.setEnabled(!s.toString().isEmpty() && isEmailValid(s.toString()));
                registerErrorMsg.setText(!isEmailValid(s.toString())? "Email is not valid": "");
            }
        });

        confirmButton.setOnClickListener(v -> sendVerification(editTextEmail.getText().toString().trim()));
    }

    private void sendVerification(String emailId) {
        progressBar.setVisibility(VISIBLE);
        firebaseAuthentication.fetchSignInMethodsForEmail(emailId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && Objects.requireNonNull(task.getResult().getSignInMethods()).size() > 0) {
                registerErrorMsg.setText(R.string.blank);
                Intent intent = new Intent(Register.this, VerifyAccountWithKey.class);
                intent.putExtra("emailId", emailId);
                startActivity(intent);
                finish();
            } else {
                registerErrorMsg.setText(R.string.email_not_linked);
            }
            progressBar.setVisibility(GONE);
        });

    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}