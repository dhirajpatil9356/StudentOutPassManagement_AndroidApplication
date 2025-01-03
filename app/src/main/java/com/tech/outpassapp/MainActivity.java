package com.tech.outpassapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
Button result;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStudentSignup = findViewById(R.id.btnStudentSignup);
        Button btnAdminSignup = findViewById(R.id.btnAdminSignup);
result=findViewById(R.id.toresult);
        btnStudentSignup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StudentSignupActivity.class));
        });

        btnAdminSignup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminSignupActivity.class));
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MainActivity.this,ResultActivity.class);
                startActivity(in);
            }
        });
    }
}
