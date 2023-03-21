package com.example.bankapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<EssentialModel> esstential_array = new ArrayList<>();
    ArrayList<RecentTransactionModel> recentTLog_array = new ArrayList<>();
    int[] essential_images = {R.drawable.ic_deposit, R.drawable.ic_loan, R.drawable.ic_savings};

    ToggleButton hide_button;
    Essential_RecyclerViewAdapter ERVAdapter;
    RecentTranscation_RVAdapter RTRVAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView essentialsrecyclerView = findViewById(R.id.estn_recyclerview);
        hideEsstentialModel();
        ERVAdapter = new Essential_RecyclerViewAdapter(this, esstential_array);
        essentialsrecyclerView.setAdapter(ERVAdapter);
        essentialsrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        RecyclerView recentTranscationsView = findViewById(R.id.recent_transcations);
        setRecentTranscationData();
        RTRVAdapter = new RecentTranscation_RVAdapter(this, recentTLog_array);
        recentTranscationsView.setAdapter(RTRVAdapter);
        recentTranscationsView.setLayoutManager(new LinearLayoutManager(this));


        hide_button = findViewById(R.id.hide_essentials);
        hide_button.setOnClickListener(view -> {
            if(hide_button.isChecked()) {
                setEssentialModel();
                ERVAdapter.notifyDataSetChanged();
            } else {
                hideEsstentialModel();
                ERVAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setRecentTranscationData() {
        String[] rtlog_names = getResources().getStringArray(R.array.recenttlog_names);
        String[] rtlog_date = getResources().getStringArray(R.array.recenttlog_date);
        String[] rtlog_amount = getResources().getStringArray(R.array.recenttlog_amount);
        String[] rtlog_type = getResources().getStringArray(R.array.recenttlog_type);

        for (int i=0; i<rtlog_names.length; i++) {
            recentTLog_array.add(new RecentTransactionModel(rtlog_names[i], rtlog_date[i], " ₹ " + rtlog_amount[i], rtlog_type[i]));
        }



    }

    private void setEssentialModel() {
        String[] essential_values = getResources().getStringArray(R.array.essentialcard_values);
        String[] essential_titles = getResources().getStringArray(R.array.essentialcard_names);
        esstential_array.clear();
        for (int i=0; i<essential_titles.length; i++) {
            esstential_array.add(new EssentialModel(essential_images[i], essential_titles[i], " ₹ " + essential_values[i]));
        }
    }

    private void hideEsstentialModel() {
        String[] essential_titles = getResources().getStringArray(R.array.essentialcard_names);
        esstential_array.clear();
        for (int i=0; i<essential_titles.length; i++) {
            esstential_array.add(new EssentialModel(essential_images[i], essential_titles[i], " XXXX"));
        }
    }
}