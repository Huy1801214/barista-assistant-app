package com.edu.server.api;

import com.edu.server.collection.UserEntity;
import com.edu.server.config.GoogleApiProperties;
import com.edu.server.dao.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;

@RestController
@RequestMapping("/api/google/auth")
public class GoogleAuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoogleApiProperties googleApiProperties;

    private GoogleAuthorizationCodeFlow flow;

    @PostConstruct
    public void init() {
        try {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(googleApiProperties.getClient().getId());
            System.out.println(googleApiProperties.getClient().getId());
            web.setClientSecret(googleApiProperties.getClient().getSecret());
            System.out.println(googleApiProperties.getClient().getSecret());

            GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(web);

            flow = new GoogleAuthorizationCodeFlow.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), clientSecrets,
                    Collections.singletonList(CalendarScopes.CALENDAR_EVENTS))
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/url")
    public ResponseEntity<String> getAuthorizationUrl() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found from JWT token"));

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String randomState = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String state = randomState + ":" + currentUser.getId();

        String url = flow.newAuthorizationUrl()
                .setRedirectUri(googleApiProperties.getRedirectUri())
                .setState(state)
                .build();

        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        try {
            System.out.println(googleApiProperties.getClient().getSecret());
            System.out.println(googleApiProperties.getClient().getId());
            if (state == null || !state.contains(":")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<h1>Lỗi</h1><p>Tham số state không hợp lệ.</p>");
            }

            String[] parts = state.split(":");
            String userId = parts[parts.length - 1];

            UserEntity currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID từ tham số state."));

            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(googleApiProperties.getRedirectUri()).execute();

            String refreshToken = tokenResponse.getRefreshToken();
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<h1>Lỗi</h1><p>Không thể lấy Refresh Token. Có thể bạn đã chấp thuận trước đó. Vui lòng thử thu hồi quyền truy cập của ứng dụng trong tài khoản Google và kết nối lại.</p>");
            }

            currentUser.setGoogleRefreshToken(refreshToken);
            userRepository.save(currentUser);

            return ResponseEntity.ok("<h1>Kết nối Google Calendar thành công!</h1><p>Bạn có thể đóng cửa sổ này và quay lại ứng dụng.</p>");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<h1>Lỗi</h1><p>Có lỗi xảy ra khi xác thực với Google. Vui lòng thử lại.</p>");
        }
    }
}