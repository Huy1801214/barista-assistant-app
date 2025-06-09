package com.example.barista.data;
public class Voucher {
    private String id;
    private String name;
    private String code;
    private String validUntil; // Chuỗi định dạng "Còn hiệu lực đến dd/MM/yyyy"
    private Status status;

    // Enum để quản lý trạng thái một cách an toàn và rõ ràng
    public enum Status {
        ACTIVE,     // Đang hoạt động
        EXPIRED,    // Hết hạn
        PAUSED      // Tạm dừng
    }

    public Voucher(String name, String code, String validUntil, Status status) {
        this.name = name;
        this.code = code;
        this.validUntil = validUntil;
        this.status = status;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public Status getStatus() {
        return status;
    }
}