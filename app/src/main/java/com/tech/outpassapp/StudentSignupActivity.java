package com.tech.outpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StudentSignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
Button tologin,btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
         btnRegister = findViewById(R.id.btnRegister);
        tologin=findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(StudentSignupActivity.this, StudentLoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(StudentSignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(StudentSignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentSignupActivity.this,StudentLoginActivity.class);
                startActivity(intent);
                Toast.makeText(StudentSignupActivity.this, "Going To Login Page !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
