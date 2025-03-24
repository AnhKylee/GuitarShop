package com.example.guitarshop.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.R;
import com.example.guitarshop.adapter.ProductAdapter;
import com.example.guitarshop.data.ProductRepository;
import com.example.guitarshop.model.CartItem;
import com.example.guitarshop.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartListener {
    private RecyclerView productRecyclerView;
    private Button cartButton;
    private EditText searchEditText;
    private Spinner sortSpinner;
    private ProductRepository productRepository;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private ProductAdapter productAdapter;
    private static List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        cartButton = findViewById(R.id.cartButton);
        searchEditText = findViewById(R.id.searchEditText);
        sortSpinner = findViewById(R.id.sortSpinner);
        productRepository = new ProductRepository();

        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();

        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(filteredProductList, this);
        productRecyclerView.setAdapter(productAdapter);

        new LoadProductsTask().execute();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortProducts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }
        });

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    public class LoadProductsTask extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... voids) {
            return productRepository.getProducts();
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (products != null && !products.isEmpty()) {
                productList.clear();
                productList.addAll(products);
                filteredProductList.clear();
                filteredProductList.addAll(productList);
                productAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ProductActivity.this, "Không thể tải sản phẩm từ SQL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void filterProducts(String query) {
        filteredProductList.clear();
        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
        }
        sortProducts(sortSpinner.getSelectedItemPosition());
        productAdapter.notifyDataSetChanged();
    }

    private void sortProducts(int sortOption) {
        switch (sortOption) {
            case 0: // Mặc định
                filteredProductList.clear();
                filteredProductList.addAll(productList);
                break;
            case 1: // Giá: Thấp đến Cao
                Collections.sort(filteredProductList, Comparator.comparingDouble(Product::getPrice));
                break;
            case 2: // Giá: Cao đến Thấp
                Collections.sort(filteredProductList, Comparator.comparingDouble(Product::getPrice).reversed());
                break;
            case 3: // Tồn kho: Thấp đến Cao
                Collections.sort(filteredProductList, Comparator.comparingInt(Product::getStock));
                break;
            case 4: // Tồn kho: Cao đến Thấp
                Collections.sort(filteredProductList, Comparator.comparingInt(Product::getStock).reversed());
                break;
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddToCart(Product product) {
        cartItems.add(new CartItem(product.getProductId(), product.getName(), product.getPrice(), 1));
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }
}