package com.edu.server.service;

import com.edu.server.collection.VoucherEntity;
import com.edu.server.dao.UserRepository;
import com.edu.server.dao.VoucherRepository;
import com.edu.server.dto.VoucherDto; // Giả sử đã có DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserRepository userRepository; // Dùng để lấy storeId

    // Lấy thông tin cửa hàng của người dùng đang đăng nhập
    private String getCurrentUserStoreId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getStoreId();
    }

    // Lấy tất cả voucher của cửa hàng
    public List<VoucherEntity> getVouchersForCurrentUser() {
        String storeId = getCurrentUserStoreId();
        return voucherRepository.findByStoreId(storeId);
    }

    public List<VoucherEntity> getVoucherForCurrentUser(String code) {
        String storeId = getCurrentUserStoreId();
        return voucherRepository.findByCode(storeId, code);
    }

    // Tạo voucher mới
    public VoucherEntity createVoucher(VoucherDto voucherDto) {
        String storeId = getCurrentUserStoreId();

        // Kiểm tra mã voucher đã tồn tại chưa
        if (voucherRepository.existsByStoreIdAndCode(storeId, voucherDto.getCode())) {
            throw new IllegalArgumentException("Voucher code '" + voucherDto.getCode() + "' already exists in this store.");
        }

        VoucherEntity newVoucher = new VoucherEntity();
        newVoucher.setName(voucherDto.getName());
        newVoucher.setCode(voucherDto.getCode());
        newVoucher.setType(VoucherEntity.VoucherType.valueOf(voucherDto.getType()));
        newVoucher.setValue(voucherDto.getValue());
        newVoucher.setStartDate(voucherDto.getStartDate());
        newVoucher.setEndDate(voucherDto.getEndDate());
        newVoucher.setStatus(VoucherEntity.VoucherStatus.valueOf(voucherDto.getStatus()));
        newVoucher.setStoreId(storeId); // Gán storeId của người dùng hiện tại

        return voucherRepository.save(newVoucher);
    }

    // Cập nhật voucher
    public VoucherEntity updateVoucher(String voucherId, VoucherDto voucherDto) {
        String storeId = getCurrentUserStoreId();
        VoucherEntity existingVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + voucherId));

        // **Kiểm tra bảo mật**: Đảm bảo người dùng chỉ sửa voucher của cửa hàng mình
        if (!existingVoucher.getStoreId().equals(storeId)) {
            throw new SecurityException("You do not have permission to modify this voucher.");
        }

        // Cập nhật các trường
        existingVoucher.setName(voucherDto.getName());
        existingVoucher.setType(VoucherEntity.VoucherType.valueOf(voucherDto.getType()));
        // ... cập nhật các trường khác tương tự

        return voucherRepository.save(existingVoucher);
    }

    // Xóa voucher
    public void deleteVoucher(String voucherId) {
        String storeId = getCurrentUserStoreId();
        VoucherEntity voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + voucherId));

        if (!voucher.getStoreId().equals(storeId)) {
            throw new SecurityException("You do not have permission to delete this voucher.");
        }

        voucherRepository.delete(voucher);
    }
}