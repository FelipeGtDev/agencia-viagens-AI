package dev.ia.model;

import dev.ia.Category;
import dev.ia.model.enums.BookingStatus;

import java.time.LocalDate;

public record Booking(
        Long id,
        String customerName,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        Category category
) {
}
