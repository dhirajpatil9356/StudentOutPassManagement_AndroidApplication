package com.tech.outpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private Spinner spinnerBatchSelection;
    private CalendarView calendarView;
    private Button btnConfirmDates, btnRestrictStudent;
    private EditText etRollNumber;
    private DatabaseReference databaseReference;
    private DatabaseReference restrictedRef;
    private Map<String, List<Long>> batchDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI components
        spinnerBatchSelection = findViewById(R.id.spinnerBatchSelection);
        calendarView = findViewById(R.id.calendarView);
        btnConfirmDates = findViewById(R.id.btnConfirmDates);
        etRollNumber = findViewById(R.id.etRollNumber);
        btnRestrictStudent = findViewById(R.id.btnRestrictStudent);
        Button toadminrequest=findViewById(R.id.toOutpassRequest);

        databaseReference = FirebaseDatabase.getInstance().getReference("batchDates");
        restrictedRef = FirebaseDatabase.getInstance().getReference("restrictedStudents");

        // Set up spinner with batch names
        String[] batches = {"Girls", "Computer Engineering", "Information Technology", "Artificial Intelligence", "Cyber Security", "Data Science"};
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, batches);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatchSelection.setAdapter(batchAdapter);

        batchDates = new HashMap<>();

        // Calendar date selection listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedBatch = spinnerBatchSelection.getSelectedItem().toString();
            long selectedDate = new java.util.GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();

            if (!batchDates.containsKey(selectedBatch)) {
                batchDates.put(selectedBatch, new ArrayList<>());
            }

            // Add date to batch's list
            batchDates.get(selectedBatch).add(selectedDate);
            Toast.makeText(this, "Date selected for " + selectedBatch, Toast.LENGTH_SHORT).show();
        });

        // Confirm dates button
        btnConfirmDates.setOnClickListener(v -> {
            for (Map.Entry<String, List<Long>> entry : batchDates.entrySet()) {
                String batch = entry.getKey();
                List<Long> dates = entry.getValue();
                databaseReference.child(batch).setValue(dates);
            }
            Toast.makeText(this, "Dates confirmed for each batch", Toast.LENGTH_SHORT).show();
        });

        // Restrict student button
        btnRestrictStudent.setOnClickListener(v -> {
            String rollNumber = etRollNumber.getText().toString().trim();
            if (!rollNumber.isEmpty()) {
                restrictedRef.child(rollNumber).setValue(true);
                Toast.makeText(this, "Student with roll number " + rollNumber + " is now restricted.", Toast.LENGTH_SHORT).show();
                etRollNumber.setText("");
            } else {
                Toast.makeText(this, "Please enter a roll number", Toast.LENGTH_SHORT).show();
            }
        });
        toadminrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,AdminRequestActivity.class));
                Toast.makeText(AdminActivity.this, "Going To Outpass Request Page !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}