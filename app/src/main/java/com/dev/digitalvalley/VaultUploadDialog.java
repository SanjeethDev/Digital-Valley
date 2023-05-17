package com.dev.digitalvalley;

import static android.content.ContentValues.TAG;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import java.util.Objects;

public class VaultUploadDialog extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isImageSelected = false;
    private TextView chooseImage, upload, errorMessage;
    private ProgressBar progressBar;
    private ImageView uploadPreview, backButton;
    private EditText uploadName;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_upload_dialog);

        chooseImage = findViewById(R.id.vault_upload_selectimage);
        progressBar = findViewById(R.id.vault_upload_progress_bar);
        uploadPreview = findViewById(R.id.vault_upload_preview);
        uploadName = findViewById(R.id.vault_upload_name);
        upload = findViewById(R.id.vault_upload_button);
        errorMessage = findViewById(R.id.vault_upload_error_msg);
        backButton = findViewById(R.id.vault_upload_back);

        storageReference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid());

        chooseImage.setOnClickListener(v -> openFileChooser());
        upload.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                errorMessage.setText(R.string.upload_in_progress);
            } else {
                uploadToCloud();
            }
        });
        backButton.setOnClickListener(v -> finish());
    }

    private void uploadToCloud() {
        if (isImageSelected) {
            Log.d(TAG, "uploadToCloud: 1");
            errorMessage.setText(R.string.blank);
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0),500);
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                    UploadVaultDocument uploadVaultDocument = new UploadVaultDocument(uploadName.getText().toString().trim(), uri.toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(uploadVaultDocument).addOnFailureListener(e -> errorMessage.setText(e.getLocalizedMessage()));
                });
                finish();

            }).addOnFailureListener(e -> errorMessage.setText(e.getLocalizedMessage())).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress((int) progress);
            });
        } else {
            Log.d(TAG, "uploadToCloud: 2");
            errorMessage.setText(R.string.image_not_selected);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLaunch.launch(intent);
        errorMessage.setText(R.string.blank);
    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    imageUri = result.getData().getData();
                    Glide.with(VaultUploadDialog.this).load(imageUri).into(uploadPreview);
                    isImageSelected = true;
                }
            });
}