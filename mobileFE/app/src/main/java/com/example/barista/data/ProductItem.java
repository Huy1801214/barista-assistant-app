package com.example.barista.data;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.List;

public class ProductItem {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("basePrice")
    private double basePrice; // Sử dụng BigDecimal cho độ chính xác cao

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("storeId")
    private String storeId;

    // Giữ lại cấu trúc này nếu bạn dự định dùng chức năng tùy chọn sản phẩm
    @SerializedName("options")
    private List<Option> options;


    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return imageURL;
    }

    public double getItemPrice() {
        return basePrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getStoreId() {
        return storeId;
    }

    public List<Option> getOptions() {
        return options;
    }

    // --- Setters (Không bắt buộc cho việc nhận dữ liệu, nhưng là thói quen tốt) ---
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    // --- Các Nested Class cho tùy chọn (Options) ---
    public static class Option {
        private String name;
        private List<Choice> choices;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
    }

    public static class Choice {
        private String name;
        @SerializedName("priceAdjustment")
        private BigDecimal priceAdjustment;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public BigDecimal getPriceAdjustment() { return priceAdjustment; }
        public void setPriceAdjustment(BigDecimal priceAdjustment) { this.priceAdjustment = priceAdjustment; }
    }
}