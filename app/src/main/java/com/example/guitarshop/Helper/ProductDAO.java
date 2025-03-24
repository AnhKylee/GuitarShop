package com.example.guitarshop.Helper;

import com.example.guitarshop.data.ProductRepository;
import com.example.guitarshop.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // Create (Insert Product)
    public boolean insertProduct(Product product) {
        Connection conn = ProductRepository.getConnection();
        if (conn == null) return false;

        String query = "INSERT INTO Products (Name, Description, Price, Stock, ImageURL, SellerID) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setInt(6, product.getSellerId());

            int rowsInserted = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read (Get All Products)
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = ProductRepository.getConnection();
        if (conn == null) return products;

        String query = "SELECT * FROM Products";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("ImageURL"),
                        rs.getInt("SellerID")
                );
                products.add(product);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Update Product
    public boolean updateProduct(Product product) {
        Connection conn = ProductRepository.getConnection();
        if (conn == null) return false;

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

            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete Product
    public boolean deleteProduct(int productID) {
        Connection conn = ProductRepository.getConnection();
        if (conn == null) return false;

        String query = "DELETE FROM Products WHERE ProductID=?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, productID);

            int rowsDeleted = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
