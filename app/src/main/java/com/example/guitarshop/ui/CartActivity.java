package com.example.guitarshop.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.Api.CreateOrder;
import com.example.guitarshop.OrderPayment;
import com.example.guitarshop.PaymentActivity;
import com.example.guitarshop.PaymentNotification;
import com.example.guitarshop.R;
import com.example.guitarshop.adapter.CartAdapter;
import com.example.guitarshop.model.CartItem;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private TextView totalPrice;
    private Button btnAccept;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        btnAccept = (Button) findViewById(R.id.buttonThanhToan);

        List<CartItem> cartItems = ProductActivity.getCartItems();

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(new CartAdapter(cartItems));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
    }

    private void createOrder() {

        CreateOrder createOrderApi = new CreateOrder();

        try {
            JSONObject data = createOrderApi.createOrder(String.valueOf(total));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", transactionId);
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "Thanh toán thành công");
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", zpTransToken);
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "cancel");
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "error");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("ERROR", Objects.requireNonNull(ex.getMessage()));
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}