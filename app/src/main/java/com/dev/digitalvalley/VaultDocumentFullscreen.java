package com.dev.digitalvalley;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class VaultDocumentFullscreen extends AppCompatActivity {
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_document_fullscreen);

        backButton = findViewById(R.id.vault_document_fullscreen_back);
        backButton.setOnClickListener(v -> finish());
    }
}