package com.example.guitarshop.data;

import com.example.guitarshop.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final String URL = "jdbc:jtds:sqlserver://10.0.2.2:1433;databaseName=GuitarShop;user=sa;password=12345;loginTimeout=30"; // Thay bằng IP, cổng, và database
    private static final String USER = "sa"; // Thay bằng username SQL Server
    private static final String PASSWORD = "12345"; // Thay bằng password SQL Server

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load JDBC Driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            // Establish connection
            connection = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Connection Error: " + e.getMessage());
        }
        return connection;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT ProductID, Name, Description, Price, Stock, ImageURL, SellerID FROM Products");

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                double price = resultSet.getDouble("Price");
                int stock = resultSet.getInt("Stock");
                String imageUrl = resultSet.getString("ImageURL");
                int sellerId = resultSet.getInt("SellerID");

                products.add(new Product(productId, name, description, price, stock, imageUrl, sellerId));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return products;
    }
}