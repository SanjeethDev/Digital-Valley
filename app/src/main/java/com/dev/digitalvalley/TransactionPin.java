package com.dev.digitalvalley;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class TransactionPin extends AppCompatActivity {
    PinView transactionPinView;
    AppCompatTextView transaction_error_msg;
    ProgressBar progressBar;
    AppCompatImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_pin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        transactionPinView = findViewById(R.id.transaction_pin_input);
        transaction_error_msg = findViewById(R.id.transaction_error_msg);
        progressBar = findViewById(R.id.transaction_progress_bar);
        logoImage = findViewById(R.id.transaction_logo);
        transactionPinView.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                progressBar.setVisibility(View.VISIBLE);
                DocumentReference accountReference = FirebaseFirestore.getInstance().document(String.format("Accounts/%s", FirebaseAuth.getInstance().getUid()));
                accountReference.get().addOnSuccessListener(documentSnapshot -> {
                    if (Objects.requireNonNull(transactionPinView.getText()).toString().equals(documentSnapshot.getString("TransactionPin"))) {
                        transaction_error_msg.setText(R.string.blank);
                        payment(getIntent().getStringExtra("amount"), getIntent().getStringExtra("receiverId"));
                    } else {
                        transaction_error_msg.setText(R.string.incorrect_pin);
                    }
                }).addOnFailureListener(e -> transaction_error_msg.setText(e.getLocalizedMessage()));
                return true;
            }
            return false;
        });
    }

    private void payment(String amount, String receiver) {
        int amountNumber = Integer.parseInt(amount);
        CollectionReference transactionsReference = FirebaseFirestore.getInstance().collection("Transactions");
        String id = transactionsReference.document().getId();
        DocumentReference transactionDocReference = transactionsReference.document(id);
        Transactions transaction = new Transactions(Timestamp.now(), FirebaseAuth.getInstance().getUid(), receiver, amountNumber);
        transactionDocReference.set(transaction).addOnSuccessListener(unused -> {
            Task<Void> updateBalanceSender = accountsReference(FirebaseAuth.getInstance().getUid()).update("Balances.Deposits", FieldValue.increment(-amountNumber));
            Task<Void> updateBalanceReceiver = accountsReference(receiver).update("Balances.Deposits", FieldValue.increment(amountNumber));
            Tasks.whenAllSuccess(updateBalanceSender, updateBalanceReceiver).addOnSuccessListener(objects -> {
                Intent intent = new Intent(TransactionPin.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                Log.d(TAG, "onFailure: " + e.getMessage());
                transaction_error_msg.setText(e.getLocalizedMessage());
            });
        }).addOnFailureListener(e -> transaction_error_msg.setText(e.getLocalizedMessage()));
    }

    private DocumentReference accountsReference(String uid) {
        return FirebaseFirestore.getInstance().document(String.format("Accounts/%s", uid));
    }
}