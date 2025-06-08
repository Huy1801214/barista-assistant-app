package com.edu.server.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.util.List;

@Document(collection = "products")
public class ProductEntity {

    @Id
    private String id;

    private String name;

    private String description;

    @Field("image_url")
    private String imageURL;

    // Luôn sử dụng BigDecimal cho các giá trị tiền tệ để tránh sai số
    @Field("base_price")
    private BigDecimal basePrice;

    // Sản phẩm này thuộc danh mục nào
    @Field("category_id")
    private String categoryId;

    // Sản phẩm này thuộc cửa hàng nào
    @Field("store_id")
    private String storeId;

    // Cấu trúc lồng (nested) để lưu các tùy chọn như Size, Topping
    private List<Option> options;

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

        // Giá trị cộng thêm hoặc trừ đi so với giá gốc
        @Field("price_adjustment")
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
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