package com.edu.server.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

// @CompoundIndex để đảm bảo mã voucher là duy nhất trong phạm vi một cửa hàng
@Document(collection = "vouchers")
@CompoundIndex(name = "store_code_idx", def = "{'store_id': 1, 'code': 1}", unique = true)
public class VoucherEntity {

    @Id
    private String id;

    private String name; // Ví dụ: "Giảm 20% tổng hóa đơn"

    private String code; // Ví dụ: "KM20"

    private VoucherType type; // Loại voucher: Giảm theo % hay số tiền cố định

    private BigDecimal value; // Giá trị: 20 (cho 20%) hoặc 50000 (cho 50.000đ)

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    private VoucherStatus status; // Trạng thái: ACTIVE, INACTIVE, EXPIRED

    @Field("store_id")
    private String storeId; // Voucher này thuộc về cửa hàng nào

    // Enum cho loại voucher
    public enum VoucherType {
        PERCENTAGE,
        FIXED_AMOUNT
    }

    // Enum cho trạng thái voucher
    public enum VoucherStatus {
        ACTIVE,
        INACTIVE
    }

    // Constructors, Getters và Setters
    // (Hãy tạo chúng bằng IDE của bạn)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public VoucherType getType() { return type; }
    public void setType(VoucherType type) { this.type = type; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public VoucherStatus getStatus() { return status; }
    public void setStatus(VoucherStatus status) { this.status = status; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
}
