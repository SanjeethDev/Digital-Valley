package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Pattern;

public class VerifyAccountWithKey extends AppCompatActivity {
    private FirebaseAuth firebaseAuthentication;
    EditText registerKey;
    ProgressBar progressBar;
    TextView errorMessage;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account_with_key);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        email = getIntent().getStringExtra("emailId");
        firebaseAuthentication = FirebaseAuth.getInstance();
        registerKey = findViewById(R.id.verify_account_key);
        progressBar = findViewById(R.id.verify_account_key_progress_bar);
        errorMessage = findViewById(R.id.verify_account_key_error_msg);
        registerKey.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!registerKey.getText().toString().isEmpty()
                            && isPasswordValid(registerKey.getText().toString().trim())) {
                        signIn(email, registerKey.getText().toString().trim());
                    } else {
                        errorMessage.setText(!registerKey.getText().toString().isEmpty()
                                && isPasswordValid(registerKey.getText().toString().trim())? "Key code is not valid": "");
                    }
                    return true;
                }
                return false;
            }
        });
        registerKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                errorMessage.setText(!isPasswordValid(s.toString())? "Key code is not valid": "");
            }
        });
    }

    private void signIn(String email, String key) {
        firebaseAuthentication.signInWithEmailAndPassword(email, key).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
                startActivity(new Intent(VerifyAccountWithKey.this, Login.class));
                finish();
           } else {
               errorMessage.setText(R.string.wrong_key);
           }
        });
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("\\d{10}");
        return !TextUtils.isEmpty(password) && pattern.matcher(password).matches();
    }
}