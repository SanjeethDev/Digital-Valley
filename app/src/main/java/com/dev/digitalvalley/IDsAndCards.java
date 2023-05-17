package com.dev.digitalvalley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IDsAndCards extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private VaultImageAdapter mAdapter;
    private ProgressBar mProgressBar;
    private DatabaseReference mDatabaseReference;
    private List<UploadVaultDocument> mUploadVaultDocuments;
    CardView functionUpload;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ids_and_cards);
        functionUpload = findViewById(R.id.vault_function_upload);
        backButton = findViewById(R.id.vault_back);
        functionUpload.setOnClickListener(v -> {
            startActivity(new Intent(IDsAndCards.this, VaultUploadDialog.class));
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressBar = findViewById(R.id.vault_progressbar);
        mRecyclerView = findViewById(R.id.vault_documents);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mUploadVaultDocuments = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploadVaultDocuments.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    UploadVaultDocument uploadVaultDocument = postSnapshot.getValue(UploadVaultDocument.class);
                    mUploadVaultDocuments.add(uploadVaultDocument);
                }
                mAdapter = new VaultImageAdapter(IDsAndCards.this, mUploadVaultDocuments);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IDsAndCards.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}