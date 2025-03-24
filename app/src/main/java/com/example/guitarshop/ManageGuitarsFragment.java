package com.example.guitarshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.guitarshop.Helper.ProductDAO;
import com.example.guitarshop.model.Product;
import java.util.List;

public class ManageGuitarsFragment extends Fragment {

    private EditText edtName, edtDescription, edtPrice, edtStock, edtImageURL, edtSellerID, edtProductID;
    private Button btnAdd, btnUpdate, btnDelete, btnViewAll;
    private ProductDAO productDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_guitars, container, false);

        // Initialize UI components
        edtProductID = view.findViewById(R.id.edtProductID);
        edtName = view.findViewById(R.id.edtName);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtStock = view.findViewById(R.id.edtStock);
        edtImageURL = view.findViewById(R.id.edtImageURL);
        edtSellerID = view.findViewById(R.id.edtSellerID);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnViewAll = view.findViewById(R.id.btnViewAll);

        productDAO = new ProductDAO();

        // Button Listeners
        btnAdd.setOnClickListener(v -> addProduct());
        btnUpdate.setOnClickListener(v -> updateProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
        btnViewAll.setOnClickListener(v -> viewAllProducts());

        return view;
    }

    // Add Product
    private void addProduct() {
        try {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            double price = Double.parseDouble(edtPrice.getText().toString().trim());
            int stock = Integer.parseInt(edtStock.getText().toString().trim());
            String imageURL = edtImageURL.getText().toString().trim();
            int sellerID = Integer.parseInt(edtSellerID.getText().toString().trim());

            Product newProduct = new Product(0, name, description, price, stock, imageURL, sellerID);
            productDAO.insertProduct(newProduct, success -> {
                if (success) {
                    Toast.makeText(getContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
            Log.e("ManageGuitarsFragment", "Add Product Error", e);
        }
    }

    // Update Product
    private void updateProduct() {
        try {
            int productID = Integer.parseInt(edtProductID.getText().toString().trim());
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            double price = Double.parseDouble(edtPrice.getText().toString().trim());
            int stock = Integer.parseInt(edtStock.getText().toString().trim());
            String imageURL = edtImageURL.getText().toString().trim();
            int sellerID = Integer.parseInt(edtSellerID.getText().toString().trim());

            Product updatedProduct = new Product(productID, name, description, price, stock, imageURL, sellerID);
            productDAO.updateProduct(updatedProduct, success -> {
                if (success) {
                    Toast.makeText(getContext(), "Sửa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Sửa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
            Log.e("ManageGuitarsFragment", "Update Product Error", e);
        }
    }

    // Delete Product
    private void deleteProduct() {
        try {
            int productID = Integer.parseInt(edtProductID.getText().toString().trim());
            productDAO.deleteProduct(productID, success -> {
                if (success) {
                    Toast.makeText(getContext(), "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
            Log.e("ManageGuitarsFragment", "Delete Product Error", e);
        }
    }

    // View All Products
    private void viewAllProducts() {
        try {
            productDAO.getAllProducts(products -> {
                if (products.isEmpty()) {
                    Toast.makeText(getContext(), "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                } else {
                    for (Product product : products) {
                        Log.d("Product", "Tên: " + product.getName() + ", Giá: " + product.getPrice());
                    }
                }
            });
        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
            Log.e("ManageGuitarsFragment", "View All Products Error", e);
        }
    }

    // Toast Utility Method
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
