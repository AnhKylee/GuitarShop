package com.example.guitarshop;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.adapter.ProductAdapter;
import com.example.guitarshop.data.ProductRepository;
import com.example.guitarshop.model.CartItem;
import com.example.guitarshop.model.Product;
import com.example.guitarshop.ui.CartActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartListener {
    private RecyclerView productRecyclerView;
    private Button cartButton;
    private Button backButton;
    private EditText searchEditText;
    private Spinner sortSpinner;
    private TextView titleTextView;
    private ProductRepository productRepository;
    private ArrayList<Product> productList; // Đổi thành ArrayList để đồng bộ
    private ArrayList<Product> filteredProductList; // Đổi thành ArrayList để đồng bộ
    private ProductAdapter productAdapter;
    private ArrayList<CartItem> cartItems; // Đổi từ List<CartItem> thành ArrayList<CartItem>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Khởi tạo các thành phần giao diện
        productRecyclerView = findViewById(R.id.productRecyclerView);
        cartButton = findViewById(R.id.cartButton);
        backButton = findViewById(R.id.backButton);
        searchEditText = findViewById(R.id.searchEditText);
        sortSpinner = findViewById(R.id.sortSpinner);
        titleTextView = findViewById(R.id.titleTextView);
        productRepository = new ProductRepository();

        // Tùy chỉnh tiêu đề cho CustomerActivity
        titleTextView.setText("Danh sách sản phẩm cho khách hàng");

        // Khởi tạo danh sách sản phẩm và giỏ hàng
        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        cartItems = new ArrayList<>();

        // Thiết lập RecyclerView
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(filteredProductList, this);
        productRecyclerView.setAdapter(productAdapter);

        // Tải danh sách sản phẩm từ SQL Server
        new LoadProductsTask().execute();

        // Thiết lập Spinner để sắp xếp sản phẩm
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
            public void onNothingSelected(AdapterView<?> parent) {
                sortProducts(0); // Mặc định
            }
        });

        // Thiết lập tìm kiếm sản phẩm
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

        // Chuyển đến CartActivity khi nhấn nút "Xem giỏ hàng"
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerActivity.this, CartActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems)); // Truyền trực tiếp cartItems
            startActivity(intent);
        });

        // Quay lại màn hình trước đó khi nhấn nút "Quay lại"
        backButton.setOnClickListener(v -> finish());
    }

    private class LoadProductsTask extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... voids) {
            try {
                return productRepository.getProducts();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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
                Toast.makeText(CustomerActivity.this, "Không thể tải sản phẩm từ SQL. Vui lòng kiểm tra kết nối mạng hoặc thông tin SQL Server.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void filterProducts(String query) {
        ArrayList<Product> tempList = new ArrayList<>();
        if (query.isEmpty()) {
            tempList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    tempList.add(product);
                }
            }
        }
        filteredProductList.clear();
        filteredProductList.addAll(tempList);
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
        Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }
}