package com.example.guitarshop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guitarshop.Api.CreateOrder;

import org.json.JSONObject;

import java.util.Objects;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderPayment extends AppCompatActivity {

    Button btnConfirm;
    TextView Soluong;
    TextView Tong;
    long currentTotal;
    int currentAmount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_payment);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnConfirm = findViewById(R.id.buttonThanhToan);
        Soluong = findViewById(R.id.textViewSoluong);
        Tong = findViewById(R.id.textViewTongTien);

        Intent intent = getIntent();

        if (intent != null) {
            int amount = intent.getIntExtra("soluong", 1);
            currentAmount = amount;
            Soluong.setText(String.valueOf(currentAmount));
            currentTotal = 1_000_000 * amount;
            Tong.setText(String.valueOf(currentTotal));
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
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
            JSONObject data = createOrderApi.createOrder(String.valueOf(currentTotal));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(OrderPayment.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", transactionId);
                        Intent intent = new Intent(OrderPayment.this, PaymentNotification.class);
                        intent.putExtra("result", "Thanh toán thành công");
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Log.d("appTransID", appTransID);
                        Log.d("transactionId", zpTransToken);
                        Intent intent = new Intent(OrderPayment.this, PaymentNotification.class);
                        intent.putExtra("result", "cancel");
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent = new Intent(OrderPayment.this, PaymentNotification.class);
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
