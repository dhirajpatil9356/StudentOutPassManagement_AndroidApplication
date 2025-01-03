package com.tech.outpassapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAccepted;
    private StudentAdapter studentAdapter;
    private ArrayList<OutpassRequest> acceptedList;
    private DatabaseReference acceptedRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerViewAccepted = findViewById(R.id.recycler_view_accepted);
        acceptedRequestsRef = FirebaseDatabase.getInstance().getReference("acceptedRequests");
        recyclerViewAccepted.setHasFixedSize(true);
        recyclerViewAccepted.setLayoutManager(new LinearLayoutManager(this));

        acceptedList = new ArrayList<>();
        studentAdapter = new StudentAdapter(this, acceptedList,false);
        recyclerViewAccepted.setAdapter(studentAdapter);

        loadAcceptedRequests();
    }

    private void loadAcceptedRequests() {
        acceptedRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptedList.clear(); // Clear previous entries
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OutpassRequest request = dataSnapshot.getValue(OutpassRequest.class);
                        if (request != null) {
                            acceptedList.add(request);
                        }
                    }
                    studentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ResultActivity.this, "No accepted requests found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
