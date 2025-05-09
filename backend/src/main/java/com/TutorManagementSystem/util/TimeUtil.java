package com.TutorManagementSystem.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {
    // Returns the next LocalDateTime for the given day of week and time string (e.g., "Monday", "10:00 AM")
    public static LocalDateTime getNextDateTime(String dayOfWeek, String timeSlot) {
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase(Locale.ENGLISH));
        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();
        int daysUntil = (targetDay.getValue() - todayDay.getValue() + 7) % 7;
        if (daysUntil == 0) daysUntil = 7; // Always get next occurrence, not today
        LocalDate nextDate = today.plusDays(daysUntil);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        LocalTime localTime = LocalTime.parse(timeSlot, timeFormatter);
        return LocalDateTime.of(nextDate, localTime);
    }
}
