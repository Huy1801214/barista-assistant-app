package com.example.barista.data;

import java.math.BigDecimal;

public class VoucherDto {
    private String name;
    private String code;
    private String type; // "PERCENTAGE" hoặc "FIXED_AMOUNT"
    private BigDecimal value;
    private String startDate; // Gửi lên dạng "yyyy-MM-dd"
    private String endDate;   // Gửi lên dạng "yyyy-MM-dd"
    private String status;  // "ACTIVE" hoặc "INACTIVE"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
