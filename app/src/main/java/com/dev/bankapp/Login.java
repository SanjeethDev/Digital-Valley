package com.dev.bankapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Toast;
import com.chaos.view.PinView;

public class Login extends AppCompatActivity {
    private PinView pin_input;
    private final String pincode = "9259";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        pin_input = findViewById(R.id.pin_input);
        BiometricPrompt biometric_prompt = new BiometricPrompt(this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                String errorMessage = "";

                switch (errorCode) {
                    case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                        errorMessage = "Biometric hardware not present";
                        break;
                    case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                        errorMessage = "Biometric hardware is unavailable";
                        break;
                    case BiometricPrompt.ERROR_NO_BIOMETRICS:
                        errorMessage = "No biometrics enrolled on this device";
                        break;
                    case BiometricPrompt.ERROR_USER_CANCELED:
                        errorMessage = "User canceled the authentication process";
                        break;
                    case BiometricPrompt.ERROR_LOCKOUT:
                        errorMessage = "Biometric authentication is temporarily locked out";
                        break;
                    case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                        errorMessage = "Biometric authentication is permanently locked out";
                        break;
                    case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                        break;
                    case BiometricPrompt.ERROR_VENDOR:
                    case BiometricPrompt.ERROR_TIMEOUT:
                    case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                        errorMessage = "Something went wrong with the biometric authentication";
                        break;
                    case BiometricPrompt.ERROR_CANCELED:
                    case BiometricPrompt.ERROR_NO_SPACE:
                    case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                        errorMessage =  "Unable to process";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + errorCode);
                }

                if (!TextUtils.isEmpty(errorMessage)) {
                    Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(Login.this, "Finger print doesn't match!", Toast.LENGTH_SHORT).show();
            }
        });
        BiometricPrompt.PromptInfo biometric_prompt_info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG).build();


        biometric_prompt.authenticate(biometric_prompt_info);

        pin_input.addTextChangedListener(new TextWatcher() {
            boolean is_complete = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pin_input.clearComposingText();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                is_complete = charSequence.length() == 4;
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String passcode = editable.toString();
                if (passcode.equals(pincode) && is_complete) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}