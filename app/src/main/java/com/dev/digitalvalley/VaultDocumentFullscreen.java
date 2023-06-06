package com.dev.digitalvalley;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;

import java.util.Objects;

public class VaultDocumentFullscreen extends AppCompatActivity {
    private DatabaseReference imageDatabaseReference;
    private String imageKey;
    private String imageUrl;
    ImageView backButton;
    TouchImageView imageView;
    TextView imageName;
    CardView deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_document_fullscreen);
        imageKey = getIntent().getStringExtra("imageKey");
        imageUrl = getIntent().getStringExtra("imageUrl");

        imageName = findViewById(R.id.vault_document_fullscreen_name);
        imageView = findViewById(R.id.vault_document_fullscreen_image);
        backButton = findViewById(R.id.vault_document_fullscreen_back);
        backButton.setOnClickListener(v -> finish());

        imageDatabaseReference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(imageKey);
        imageDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("imageName").getValue() != null) {
                    imageName.setText(snapshot.child("imageName").getValue().toString());
                    Glide.with(VaultDocumentFullscreen.this).load(snapshot.child("imageUrl").getValue().toString()).placeholder(R.mipmap.ic_launcher).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deleteButton = findViewById(R.id.vault_document_delete_card);
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VaultDocumentFullscreen.this);
            builder.setCancelable(true);
            builder.setTitle("Delete");
            builder.setIcon(R.drawable.ic_delete);

            builder.setMessage(String.format("Are you sure you want to delete '%s'?", imageName.getText()));
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        imageRef.delete().addOnSuccessListener(unused -> {
                            imageDatabaseReference.removeValue();
                            Toast.makeText(VaultDocumentFullscreen.this, String.format("%s delete.", imageName.getText()), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        });
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }
}