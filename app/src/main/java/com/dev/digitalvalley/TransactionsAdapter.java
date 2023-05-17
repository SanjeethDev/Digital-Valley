package com.dev.digitalvalley;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Locale;

public class TransactionsAdapter extends  FirestoreRecyclerAdapter<Transactions, TransactionsAdapter.RecentTransactionHolder> {
    public TransactionsAdapter(FirestoreRecyclerOptions<Transactions> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentTransactionHolder holder, int position, @NonNull Transactions model) {
        if (model.getReceiver().equals(FirebaseAuth.getInstance().getUid())) {
            holder.textViewDate.setText(formatDate(model.getDate().toDate()));
            holder.textViewType.setText("+ ₹");
            holder.textViewAmount.setTextColor(Color.GREEN);
            holder.textViewType.setTextColor(Color.GREEN);
            holder.textViewAmount.setText(String.valueOf(model.getAmount()));
            FirebaseFirestore.getInstance().document(String.format("Accounts/%s", model.getSender())).get().addOnSuccessListener(documentSnapshot -> {
                String phoneNumber = documentSnapshot.getString("PhoneNumber");
                if (phoneNumber.equals("1234567890")) {
                    String accountName = documentSnapshot.getString("FirstName")+" "+documentSnapshot.getString("LastName");
                    holder.textViewAccount.setText(accountName);
                } else if (phoneNumber.equals("0987654321")) {
                    String accountName = documentSnapshot.getString("FirstName")+" "+documentSnapshot.getString("LastName");
                    holder.textViewAccount.setText(accountName);
                } else {
                    String phoneNumberWithCode = "+91 " + phoneNumber;
                    holder.textViewAccount.setText(phoneNumberWithCode);
                }
            });
        } else {
            holder.textViewDate.setText(formatDate(model.getDate().toDate()));
            holder.textViewType.setText("- ₹");
            holder.textViewAmount.setTextColor(Color.RED);
            holder.textViewType.setTextColor(Color.RED);
            holder.textViewAmount.setText(String.valueOf(model.getAmount()));
            FirebaseFirestore.getInstance().document(String.format("Accounts/%s", model.getReceiver())).get().addOnSuccessListener(documentSnapshot -> {
                String phoneNumber = documentSnapshot.getString("PhoneNumber");
                if (phoneNumber.equals("1234567890")) {
                    String accountName = documentSnapshot.getString("FirstName")+" "+documentSnapshot.getString("LastName");
                    holder.textViewAccount.setText(accountName);
                } else if (phoneNumber.equals("0987654321")) {
                    String accountName = documentSnapshot.getString("FirstName")+" "+documentSnapshot.getString("LastName");
                    holder.textViewAccount.setText(accountName);
                } else {
                    String phoneNumberWithCode = "+91 " + phoneNumber;
                    holder.textViewAccount.setText(phoneNumberWithCode);
                }
            });
        }

    }

    @NonNull
    @Override
    public RecentTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_itemview, parent, false);
        return new RecentTransactionHolder(v);
    }

    static class RecentTransactionHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewType, textViewAmount, textViewAccount;

        public RecentTransactionHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.itemview_transaction_datetime);
            textViewType = itemView.findViewById(R.id.itemview_transaction_type);
            textViewAmount = itemView.findViewById(R.id.itemview_transaction_amount);
            textViewAccount = itemView.findViewById(R.id.itemview_transaction_account);
        }
    }

    private String formatDate(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", Locale.getDefault());
        String formattedDate = sdf.format(dateTime);
        return formattedDate;
    }
}
