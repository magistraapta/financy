package com.financy.financy.transaction.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateStr = parser.getText();
        try {
            // Try parsing as full datetime first
            return LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            try {
                // If that fails, try parsing as date only and set time to midnight
                LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
                return LocalDateTime.of(date, LocalTime.MIDNIGHT);
            } catch (Exception ex) {
                throw new IOException("Failed to parse date: " + dateStr, ex);
            }
        }
    }
} 