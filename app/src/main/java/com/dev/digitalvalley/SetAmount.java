package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class SetAmount extends AppCompatActivity {
    AppCompatTextView fullName, phoneNumber;
    AppCompatImageView backButton;
    AppCompatEditText amountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_amount);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        backButton = findViewById(R.id.setamount_back);
        fullName = findViewById(R.id.setamount_fullname);
        phoneNumber = findViewById(R.id.setamount_phone_number);
        amountEditText = findViewById(R.id.setamount_amount);

        fullName.setText(getIntent().getStringExtra("fullName"));
        phoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        backButton.setOnClickListener(v -> {
            finish();
        });

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    amountEditText.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                Intent intent = new Intent(SetAmount.this, TransactionPin.class);
                                intent.putExtra("receiverId", getIntent().getStringExtra("receiverId"));
                                intent.putExtra("amount", s.toString().trim());
                                startActivity(intent);
                            }
                            return false;
                        }
                    });
                } else {

                }
            }
        });
    }

}