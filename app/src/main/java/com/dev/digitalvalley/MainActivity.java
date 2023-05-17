package com.dev.digitalvalley;

import static java.lang.Integer.parseInt;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuthentication;
    private DocumentReference currentAccountReference;
    private CollectionReference transactionReference;

    private TransactionsAdapter recentTransactionsAdapter;
    RecyclerView recentTransactionsView;

    LinearLayout
            functionSendPhone,
            functionScanQrCode,
            functionPayContacts,functionIDsAndCards;
    TextView
            displayUsername,
            currentBalance;
    AppCompatTextView showAllTransactions;
    AppCompatImageView logoutButton;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuthentication = FirebaseAuth.getInstance();
        FirebaseFirestore firestoreDatabase = FirebaseFirestore.getInstance();
        currentAccountReference = firestoreDatabase.document(String.format("Accounts/%s",getUserId()));
        transactionReference = firestoreDatabase.collection("Transactions");


        displayUsername = findViewById(R.id.main_username);
        currentBalance = findViewById(R.id.main_current_balance_amount);
        functionSendPhone = findViewById(R.id.main_function_send_phone);
        functionScanQrCode = findViewById(R.id.main_function_scan_qr);
        functionPayContacts = findViewById(R.id.main_function_pay_contacts);
        functionIDsAndCards = findViewById(R.id.main_function_idsandcards);
        recentTransactionsView = findViewById(R.id.recent_transactions);
        showAllTransactions = findViewById(R.id.main_show_all_transactions);
        logoutButton = findViewById(R.id.main_logout_button);

        displayUsername.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AccountInfo.class)));

        setUpRecyclerView();
        showAllTransactions.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AllTransactions.class)));

        functionSendPhone.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PayByPhone.class)));

        functionScanQrCode.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanQR.class)));

        functionPayContacts.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PayContacts.class)));

        functionIDsAndCards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, IDsAndCards.class)));

        currentAccountReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                displayUsername.setText(String.format("%s %s", documentSnapshot.getString("FirstName"), documentSnapshot.getString("LastName")));
            } else {
                Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());

        logoutButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setUpRecyclerView() {
        Query query = transactionReference
                .where(Filter.or(
                        Filter.equalTo("sender", getUserId()),
                        Filter.equalTo("receiver", getUserId()))).orderBy("date", Query.Direction.DESCENDING)
                .limit(5);
        FirestoreRecyclerOptions<Transactions> options = new FirestoreRecyclerOptions.Builder<Transactions>()
                .setQuery(query, Transactions.class).build();
        recentTransactionsAdapter = new TransactionsAdapter(options);
        RecyclerView recentTransactionsRecyclerView = findViewById(R.id.recent_transactions);
        recentTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recentTransactionsRecyclerView.setAdapter(recentTransactionsAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        currentAccountReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            if (value.exists()) {
                Map<String, Object> data = (Map<String, Object>) value.get("Balances");
                assert data != null;
                int deposits = parseInt(Objects.requireNonNull(data.get("Deposits")).toString());
                int borrowings = parseInt(Objects.requireNonNull(data.get("Borrowings")).toString());
                int savings = deposits + borrowings;
                currentBalance.setText(String.format("₹ %s", savings));
            }
        });
        recentTransactionsAdapter.startListening();
        recentTransactionsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recentTransactionsAdapter.stopListening();
    }

    @NonNull
    private String getUserId() {
        return Objects.requireNonNull(firebaseAuthentication
                        .getCurrentUser())
                .getUid();
    }
}