package com.example.guitarshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.guitarshop.R;
import com.example.guitarshop.data.OrderItem;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderItem> {
    private Context context;
    private List<OrderItem> orderList;

    public OrderAdapter(@NonNull Context context, @NonNull List<OrderItem> objects) {
        super(context, R.layout.item_order, objects);
        this.context = context;
        this.orderList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        OrderItem item = orderList.get(position);

        TextView txtProductName = convertView.findViewById(R.id.txtProductName);
        TextView txtProductPrice = convertView.findViewById(R.id.txtProductPrice);
        TextView txtProductQuantity = convertView.findViewById(R.id.txtProductQuantity);
        Button btnRemove = convertView.findViewById(R.id.btnRemove);

        txtProductName.setText(item.getName());
        txtProductPrice.setText(item.getPrice() + " VND");
        txtProductQuantity.setText("SL: " + item.getQuantity());

        btnRemove.setOnClickListener(view -> {
            orderList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}