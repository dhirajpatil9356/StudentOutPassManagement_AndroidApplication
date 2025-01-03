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

public class AdminRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private ArrayList<OutpassRequest> studentList;
    private DatabaseReference databaseReference;
    private TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        recyclerView = findViewById(R.id.recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("outpassRequests");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(this, studentList,true);
        recyclerView.setAdapter(studentAdapter);
        noDataText = findViewById(R.id.no_data_text);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear(); // Clear the list to avoid duplicate entries
                if (snapshot.exists()) {
                    noDataText.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OutpassRequest request = dataSnapshot.getValue(OutpassRequest.class);
                        if (request != null) {
                            studentList.add(request);
                        }
                    }
                    studentAdapter.notifyDataSetChanged();
                } else {
                    noDataText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminRequestActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
