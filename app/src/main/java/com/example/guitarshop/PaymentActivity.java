package com.example.guitarshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    Button btnConfirm;
    EditText edtSoluong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        btnConfirm = findViewById(R.id.buttonConfirm);
        edtSoluong = findViewById(R.id.editTextSoluong);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSoluong.getText() == null || edtSoluong.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Nhập số lượng muốn mua", Toast.LENGTH_SHORT).show();
                    return;
                }

                String s = edtSoluong.getText().toString();
                int amount  = Integer.parseInt(s);

                Intent intent = new Intent(PaymentActivity.this, OrderPayment.class);
                intent.putExtra("soluong", amount);
                startActivity(intent);
            }
        });
    }
}
