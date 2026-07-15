package dev.ia.services;

import dev.ia.model.Booking;

import java.util.Optional;

public interface BookingService {
    Optional<Booking> getBookingDetails(String bookingId);

    Optional<Booking> cancelBooking(long bookingId, String customerLastName);
}
