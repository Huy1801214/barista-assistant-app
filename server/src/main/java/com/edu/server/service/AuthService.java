package com.edu.server.service;

import com.edu.server.collection.StoreEntity; // Giả sử bạn đã có StoreEntity
import com.edu.server.collection.UserEntity;
import com.edu.server.dao.StoreRepository; // Giả sử bạn đã có StoreRepository
import com.edu.server.dao.UserRepository;
import com.edu.server.dto.LoginRequest;
import com.edu.server.dto.LoginResponse;
import com.edu.server.dto.RegisterRequest;
import com.edu.server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // TODO: Inject JwtTokenProvider cho phần đăng nhập

    /**
     * Xử lý logic đăng ký tài khoản mới.
     * Đây là một transaction: hoặc tất cả thành công, hoặc không có gì được lưu.
     */
    @Transactional
    public UserEntity register(RegisterRequest registerRequest) {
        // 1. Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // 2. Tạo một cửa hàng mới
        StoreEntity newStore = new StoreEntity();
        newStore.setName(registerRequest.getStoreName());
        // Các thông tin khác cho cửa hàng...
        StoreEntity savedStore = storeRepository.save(newStore);

        // 3. Tạo người dùng mới
        UserEntity newUser = new UserEntity();
        newUser.setFullName(registerRequest.getFullName());
        newUser.setEmail(registerRequest.getEmail());
        // Mã hóa mật khẩu trước khi lưu
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());

        // 4. Thiết lập vai trò và liên kết với cửa hàng
        newUser.setRole(UserEntity.Role.OWNER); // Người đăng ký đầu tiên là chủ cửa hàng
        newUser.setStoreId(savedStore.getId()); // Gán ID của cửa hàng vừa tạo

        // 5. Cập nhật lại ownerId cho cửa hàng
        savedStore.setOwnerId(newUser.getId()); // Cần ID của user, nên phải save user trước

        UserEntity savedUser = userRepository.save(newUser);
        savedStore.setOwnerId(savedUser.getId());
        storeRepository.save(savedStore);

        return savedUser;
    }

    /**
     * Xử lý logic đăng nhập
     */
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo JWT Token
        String jwt = tokenProvider.generateToken(authentication);

        // Lấy thông tin chi tiết của người dùng từ DB để trả về
        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        // Tạo đối tượng UserInfo
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(userEntity.getId());
        userInfo.setFullName(userEntity.getFullName());
        userInfo.setEmail(userEntity.getEmail());
        userInfo.setRole(userEntity.getRole().name()); // Chuyển enum thành String
        userInfo.setStoreId(userEntity.getStoreId());

        // Trả về đối tượng LoginResponse hoàn chỉnh
        return new LoginResponse(jwt, userInfo);
    }
}