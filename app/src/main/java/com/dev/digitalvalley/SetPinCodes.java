package com.dev.digitalvalley;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.chaos.view.PinView;

public class SetPinCodes extends AppCompatActivity {
    PinView registerLogin, confirmLogin, registerTransaction, confirmTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pincodes);
        registerLogin = findViewById(R.id.register_login_pin);
        confirmLogin = findViewById(R.id.confirm_login_pin);
        registerTransaction = findViewById(R.id.register_transaction_pin);
        confirmTransaction = findViewById(R.id.confirm_transaction_pin);
    }
}