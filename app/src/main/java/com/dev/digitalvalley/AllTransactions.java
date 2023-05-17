package com.dev.digitalvalley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllTransactions extends AppCompatActivity {
    private CollectionReference transactionReference;
    private TransactionsAdapter transactionsAdapter;
    RecyclerView transactionsView;
    AppCompatImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        backButton = findViewById(R.id.all_transactions_back);
        transactionsView = findViewById(R.id.all_transactions);
        transactionReference = FirebaseFirestore.getInstance().collection("Transactions");

        backButton.setOnClickListener(v -> {
            finish();
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = transactionReference
                .where(Filter.or(
                        Filter.equalTo("sender", FirebaseAuth.getInstance().getUid()),
                        Filter.equalTo("receiver", FirebaseAuth.getInstance().getUid()))).orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Transactions> options = new FirestoreRecyclerOptions.Builder<Transactions>()
                .setQuery(query, Transactions.class).build();
        transactionsAdapter = new TransactionsAdapter(options);
        transactionsView.setLayoutManager(new LinearLayoutManager(AllTransactions.this));
        transactionsView.setAdapter(transactionsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        transactionsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        transactionsAdapter.stopListening();
    }
}