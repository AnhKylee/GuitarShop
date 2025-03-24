package com.example.guitarshop.Helper;

import com.example.guitarshop.data.ProductRepository;
import com.example.guitarshop.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDAO {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Insert Product (Asynchronous)
    public void insertProduct(Product product, Callback<Boolean> callback) {
        executor.execute(() -> {
            boolean success = false;
            Connection conn = ProductRepository.getConnection();
            if (conn != null) {
                String query = "INSERT INTO Products (Name, Description, Price, Stock, ImageURL, SellerID) VALUES (?, ?, ?, ?, ?, ?)";
                try {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, product.getName());
                    stmt.setString(2, product.getDescription());
                    stmt.setDouble(3, product.getPrice());
                    stmt.setInt(4, product.getStock());
                    stmt.setString(5, product.getImageUrl());
                    stmt.setInt(6, product.getSellerId());
                    success = stmt.executeUpdate() > 0;
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            callback.onComplete(success);
        });
    }

    // Get All Products (Asynchronous)
    public void getAllProducts(Callback<List<Product>> callback) {
        executor.execute(() -> {
            List<Product> products = new ArrayList<>();
            Connection conn = ProductRepository.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM Products";
                try {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        products.add(new Product(
                                rs.getInt("ProductID"),
                                rs.getString("Name"),
                                rs.getString("Description"),
                                rs.getDouble("Price"),
                                rs.getInt("Stock"),
                                rs.getString("ImageURL"),
                                rs.getInt("SellerID")));
                    }
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            callback.onComplete(products);
        });
    }

    // Update Product (Asynchronous)
    public void updateProduct(Product product, Callback<Boolean> callback) {
        executor.execute(() -> {
            boolean success = false;
            Connection conn = ProductRepository.getConnection();
            if (conn != null) {
                String query = "UPDATE Products SET Name=?, Description=?, Price=?, Stock=?, ImageURL=?, SellerID=? WHERE ProductID=?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, product.getName());
                    stmt.setString(2, product.getDescription());
                    stmt.setDouble(3, product.getPrice());
                    stmt.setInt(4, product.getStock());
                    stmt.setString(5, product.getImageUrl());
                    stmt.setInt(6, product.getSellerId());
                    stmt.setInt(7, product.getProductId());
                    success = stmt.executeUpdate() > 0;
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            callback.onComplete(success);
        });
    }

    // Delete Product (Asynchronous)
    public void deleteProduct(int productID, Callback<Boolean> callback) {
        executor.execute(() -> {
            boolean success = false;
            Connection conn = ProductRepository.getConnection();
            if (conn != null) {
                String query = "DELETE FROM Products WHERE ProductID=?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, productID);
                    success = stmt.executeUpdate() > 0;
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            callback.onComplete(success);
        });
    }

    // Callback interface for async operations
    public interface Callback<T> {
        void onComplete(T result);
    }
}
