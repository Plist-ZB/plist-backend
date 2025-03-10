package com.zerobase.plistbackend.module.user.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public abstract class TimeValueFormatter {

    private AssertionError TimeValueFormatter() {
        return new AssertionError("생성 금지");
    }

    public static String formatToString(Timestamp start, Timestamp end) {
        LocalDateTime startDate = start.toLocalDateTime();
        LocalDateTime endDate = end.toLocalDateTime();

        long totalSeconds = Duration.between(startDate, endDate).getSeconds();

        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);

        return String.format("%d시간 %d분", hours, minutes);
    }
}
