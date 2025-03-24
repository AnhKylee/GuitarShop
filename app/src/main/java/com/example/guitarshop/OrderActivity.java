package com.example.guitarshop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guitarshop.adapter.OrderAdapter;
import com.example.guitarshop.data.OrderItem;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private ListView listViewOrder;
    private TextView txtTotalPrice;
    private RadioGroup radioGroupPayment;
    private Button btnConfirmOrder;

    private ArrayList<OrderItem> orderItems;
    private OrderAdapter orderAdapter;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        listViewOrder = findViewById(R.id.listViewOrder);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // gia lap danh sach dat hang
        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("Guitar Yamaha", 3000000, 1));
        orderItems.add(new OrderItem("Guitar Fender", 5000000, 1));

        orderAdapter = new OrderAdapter(this, orderItems);
        listViewOrder.setAdapter(orderAdapter);

        updateTotalPrice();

        btnConfirmOrder.setOnClickListener(view -> confirmOrder());
    }

    private void updateTotalPrice() {
        totalPrice = 0;
        for (OrderItem item : orderItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        txtTotalPrice.setText("Tổng tiền: " + totalPrice + " VND");
    }

    private void confirmOrder() {
        int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedPaymentId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

    }
}
