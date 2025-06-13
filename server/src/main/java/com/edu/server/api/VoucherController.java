package com.edu.server.api;

import com.edu.server.collection.VoucherEntity;
import com.edu.server.dto.VoucherDto;
import com.edu.server.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    // API lấy tất cả voucher của cửa hàng (tất cả nhân viên đều xem được)
    @GetMapping
    public ResponseEntity<List<VoucherEntity>> getAllVouchers(@RequestParam(name = "code", required = false) String code) {

        if (code != null) {
            return ResponseEntity.ok(voucherService.getVoucherForCurrentUser(code));
        }
        List<VoucherEntity> vouchers = voucherService.getVouchersForCurrentUser();
        return ResponseEntity.ok(vouchers);
    }

    // API tạo voucher mới (chỉ Owner hoặc Manager)
    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDto voucherDto) {
        try {
            VoucherEntity createdVoucher = voucherService.createVoucher(voucherDto);
            return ResponseEntity.ok(createdVoucher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API cập nhật voucher (chỉ Owner hoặc Manager)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<VoucherEntity> updateVoucher(@PathVariable String id, @RequestBody VoucherDto voucherDto) {
        VoucherEntity updatedVoucher = voucherService.updateVoucher(id, voucherDto);
        return ResponseEntity.ok(updatedVoucher);
    }

    // API xóa voucher (chỉ Owner hoặc Manager)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> deleteVoucher(@PathVariable String id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
}
