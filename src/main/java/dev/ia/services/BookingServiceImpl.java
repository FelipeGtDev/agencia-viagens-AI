package dev.ia.services;

import dev.ia.model.Booking;
import dev.ia.model.enums.BookingStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class BookingServiceImpl implements BookingService {

    private final Map<Long, Booking> bookings = new HashMap<>();

    public BookingServiceImpl() {
        bookings.put(12345L, new Booking(12345L, "John Doe", "Tesouros do Egito",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(14), BookingStatus.PENDING));
        bookings.put(67890L, new Booking(67890L, "Jane Smith", "Aventura Amazônia",
                LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(3).plusDays(10), BookingStatus.PENDING));
    }


    @Override
    public Optional<Booking> getBookingDetails(String bookingId) {
        return Optional.ofNullable(bookings.get(Long.valueOf(bookingId)));
    }

    @Override
    public Optional<Booking> cancelBooking(long bookingId, String customerLastName) {
        if (bookings.containsKey(bookingId)) {
            Booking booking = bookings.get(bookingId);
            if (booking.customerName().endsWith(customerLastName)) {
                Booking cancelledBooking = new Booking(booking.id(), booking.customerName(), booking.destination(),
                        booking.startDate(), booking.endDate(), BookingStatus.CANCELLED);
                bookings.put(bookingId, cancelledBooking);
                return Optional.of(cancelledBooking);

            }
        }
        return Optional.empty();
    }
}