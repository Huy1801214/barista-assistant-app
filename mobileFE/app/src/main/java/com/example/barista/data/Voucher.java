package com.example.barista.data;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Voucher {

    // Các annotation này giúp Gson ánh xạ tên trường trong JSON vào thuộc tính của class
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("endDate")
    private String endDate; // Nhận về dạng "yyyy-MM-dd"

    @SerializedName("status")
    private String status; // Nhận về dạng "ACTIVE", "INACTIVE"

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getStatus() { return status; }


    // === CÁC PHƯƠNG THỨC TIỆN ÍCH ĐỂ HIỂN THỊ TRONG ADAPTER ===

    // Phương thức này chuyển đổi ngày tháng để hiển thị đẹp hơn
    public String getFormattedValidUntil() {
        if (endDate == null) return "Không có ngày hết hạn";
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date = apiFormat.parse(endDate);
            return "Còn hiệu lực đến " + displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Ngày không hợp lệ";
        }
    }

    // Phương thức này xác định trạng thái cuối cùng để hiển thị
    public Status getDisplayStatus() {
        if ("INACTIVE".equals(status)) {
            return Status.PAUSED;
        }

        // Kiểm tra xem đã hết hạn chưa, bất kể trạng thái ACTIVE
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date expiryDate = apiFormat.parse(endDate);
            // So sánh ngày hết hạn với ngày hiện tại
            if (new Date().after(expiryDate)) {
                return Status.EXPIRED;
            }
        } catch (ParseException e) {
            return Status.PAUSED; // Coi như không hợp lệ nếu không parse được ngày
        }

        return Status.ACTIVE;
    }

    public enum Status {
        ACTIVE,
        EXPIRED,
        PAUSED
    }
}