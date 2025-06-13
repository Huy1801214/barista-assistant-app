package com.example.barista.data;

import java.math.BigDecimal;
import java.util.List;

public class ProductItem {
    private String id;

    private String name;

    private String description;

    private String imageURL;

    private double basePrice;
    private String categoryId;

    private String storeId;

    // Cấu trúc lồng (nested) để lưu các tùy chọn như Size, Topping
    private List<Option> options;

    public String getItemId() {
        return id;
    }

    public String getItemName() {
        return name;

    }

    public double getItemPrice() {
        return basePrice;
    }

    public String getThumbnailUrl() {
        return imageURL;
    }

    // Nested class để định nghĩa một nhóm tùy chọn (ví dụ: "Size", "Đường", "Đá")
    public static class Option {
        private String name; // Tên nhóm: "Size", "Topping"
        private List<Choice> choices; // Danh sách các lựa chọn trong nhóm

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
    }

    // Nested class để định nghĩa một lựa chọn cụ thể
    public static class Choice {
        private String name; // Tên lựa chọn: "Nhỏ", "Lớn", "Trân châu đen"

        private BigDecimal priceAdjustment;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public BigDecimal getPriceAdjustment() { return priceAdjustment; }
        public void setPriceAdjustment(BigDecimal priceAdjustment) { this.priceAdjustment = priceAdjustment; }
    }

    // Constructors, Getters và Setters cho class Product...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
