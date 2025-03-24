package com.example.guitarshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //Tam chua co login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCustomer = findViewById(R.id.btnCustomer);
        Button btnManager = findViewById(R.id.btnManager);

        btnManager.setOnClickListener(view -> startActivity(new Intent(this, ManagerActivity.class)));
        btnCustomer.setOnClickListener(view -> startActivity(new Intent(this, CustomerActivity.class)));
    }
}