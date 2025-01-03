package com.tech.outpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentRequestActivity extends AppCompatActivity {

    private EditText etStudentName, etRollNumber, etStudentID, etRoomNumber;
    private Spinner spinnerBranch;
    private Button btnSubmitRequest;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference restrictedRef;
    private Map<String, int[]> batchDateMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outpass_request);

        etStudentName = findViewById(R.id.etStudentName);
        etRollNumber = findViewById(R.id.etRollNumber);
        etStudentID = findViewById(R.id.etStudentID);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        spinnerBranch = findViewById(R.id.spinnerBranch);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("outpassRequests");
        restrictedRef = FirebaseDatabase.getInstance().getReference("restrictedStudents");

        String[] branches = {"Computer Engineering", "Information Technology", "Artificial Intelligence", "Cyber Security", "Data Science"};
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(branchAdapter);

        // Initialize batch-to-date mapping
        setupBatchDateMapping();

        btnSubmitRequest.setOnClickListener(v -> checkEligibilityAndSubmit());
    }

    private void setupBatchDateMapping() {
        batchDateMapping = new HashMap<>();
        batchDateMapping.put("Girls", new int[]{1, 8, 15});
        batchDateMapping.put("Computer Engineering", new int[]{2, 9, 16});
        batchDateMapping.put("Information Technology", new int[]{3, 10, 17});
        batchDateMapping.put("Artificial Intelligence", new int[]{4, 14, 18});
        batchDateMapping.put("Cyber Security", new int[]{5, 12, 19});
        batchDateMapping.put("Data Science", new int[]{6, 13, 20});
    }

    private void checkEligibilityAndSubmit() {
        String studentName = etStudentName.getText().toString().trim();
        String rollNumber = etRollNumber.getText().toString().trim();
        String studentID = etStudentID.getText().toString().trim();
        String roomNumber = etRoomNumber.getText().toString().trim();
        String branch = spinnerBranch.getSelectedItem().toString();

        if (TextUtils.isEmpty(studentName) || TextUtils.isEmpty(rollNumber) ||
                TextUtils.isEmpty(studentID) || TextUtils.isEmpty(roomNumber) || TextUtils.isEmpty(branch)) {
            Toast.makeText(StudentRequestActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the student is restricted
        restrictedRef.child(rollNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(StudentRequestActivity.this, "You are restricted from applying for an outpass.", Toast.LENGTH_SHORT).show();
                } else {
                    // Check eligibility based on batch dates
                    if (isEligibleForToday(branch)) {
                        submitOutpassRequest(studentName, rollNumber, studentID, roomNumber, branch);
                    } else {
                        Toast.makeText(StudentRequestActivity.this, "You are not eligible to apply today.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StudentRequestActivity.this, "Error checking restriction: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEligibleForToday(String branch) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int[] allowedDates = batchDateMapping.getOrDefault(branch, new int[]{});

        return Arrays.stream(allowedDates).anyMatch(date -> date == today);
    }

    private void submitOutpassRequest(String studentName, String rollNumber, String studentID, String roomNumber, String branch) {
        String requestId = rollNumber;  // Use roll number as unique ID
        OutpassRequest request = new OutpassRequest(studentName, rollNumber, studentID, roomNumber, branch);

        databaseReference.child(requestId).setValue(request)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(StudentRequestActivity.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    } else {
                        Toast.makeText(StudentRequestActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
