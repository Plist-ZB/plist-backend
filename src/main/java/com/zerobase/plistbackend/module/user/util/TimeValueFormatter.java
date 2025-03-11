package com.zerobase.plistbackend.module.user.util;

import org.springframework.stereotype.Component;

@Component
public abstract class TimeValueFormatter {

    private AssertionError TimeValueFormatter() {
        return new AssertionError("생성 금지");
    }

    public static String formatToString(long totalSeconds) {
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        return String.format("%d시간 %d분", hours, minutes);
    }
}
