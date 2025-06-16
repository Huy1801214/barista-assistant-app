package com.edu.server.service;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Value;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GoogleCalendarService {


    private static final String APPLICATION_NAME = "My POS App";

    public void createEvent(String clientId, String clientSecret, String refreshToken, String summary, String description, LocalDateTime startTime, LocalDateTime endTime) throws IOException {

        // 1. Tạo Credential từ refresh token
        Credential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        // 2. Tạo đối tượng service Calendar
        Calendar service = new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 3. Tạo đối tượng Event
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTimeFormatter rfc3339Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedStartTime = startTime.format(rfc3339Formatter);
        String formattedEndTime = endTime.format(rfc3339Formatter);

        // 4. Set thời gian
        DateTime startDateTime = new DateTime(formattedStartTime);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Asia/Ho_Chi_Minh");
        event.setStart(start);

        DateTime endDateTime = new DateTime(formattedEndTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Asia/Ho_Chi_Minh");
        event.setEnd(end);

        // 5. Insert sự kiện vào lịch chính của người dùng
        String calendarId = "primary";
        service.events().insert(calendarId, event).execute();
    }
}