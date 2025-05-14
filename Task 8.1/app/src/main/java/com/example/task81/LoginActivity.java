package com.example.task81;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        btnGo = findViewById(R.id.btnGo);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Start chat activity with username
                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
    }
} 