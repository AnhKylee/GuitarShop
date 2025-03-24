package com.example.guitarshop.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private int sellerId;

    public Product(int productId, String name, String description, double price, int stock, String imageUrl, int sellerId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public int getSellerId() { return sellerId; }
}