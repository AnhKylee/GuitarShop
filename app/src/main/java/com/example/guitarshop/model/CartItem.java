//package com.example.guitarshop.model;
//
//public class CartItem {
//    private int productId;
//    private String name;
//    private double price;
//    private int quantity;
//
//    public CartItem(int productId, String name, double price, int quantity) {
//        this.productId = productId;
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//    }
//
//    public int getProductId() { return productId; }
//    public String getName() { return name; }
//    public double getPrice() { return price; }
//    public int getQuantity() { return quantity; }
//}

package com.example.guitarshop.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private int productId;
    private String name;
    private double price;
    private int quantity;

    public CartItem(int productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter methods
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Parcelable Implementation
    protected CartItem(Parcel in) {
        productId = in.readInt();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
