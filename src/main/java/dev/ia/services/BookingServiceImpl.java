package dev.ia.services;

import dev.ia.Category;
import dev.ia.model.Booking;
import dev.ia.model.enums.BookingStatus;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class BookingServiceImpl implements BookingService {

    @Inject
    SecurityIdentity securityIdentity;

    private final Map<Long, Booking> bookings = new HashMap<>();

    public BookingServiceImpl() {
        // Dados de exemplo, com um usuário correspondente ao usuário de teste
        bookings.put(12345L, new Booking(12345L, "testuser", "Tesouros do Egito",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(10),
                BookingStatus.CONFIRMED, Category.TREASURES));
        bookings.put(67890L, new Booking(67890L, "Jane Smith", "Aventura Amazônia",
                LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(3).plusDays(7),
                BookingStatus.CONFIRMED, Category.ADVENTURE));
        bookings.put(98765L, new Booking(98765L, "Peter Jones", "Trilha Inca",
                LocalDate.now().plusMonths(4), LocalDate.now().plusMonths(4).plusDays(8),
                BookingStatus.CONFIRMED, Category.ADVENTURE));
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
                        booking.startDate(), booking.endDate(), BookingStatus.CANCELLED, booking.category());
                bookings.put(bookingId, cancelledBooking);
                return Optional.of(cancelledBooking);

            }
        }
        return Optional.empty();
    }

    public Optional<Booking> cancelBooking(long bookingId) {
        // Obtém o nome do principal (usuário) do contexto de segurança injetado
        String currentUser = securityIdentity.getPrincipal().getName();

        if (bookings.containsKey(bookingId)) {
            Booking booking = bookings.get(bookingId);

            // Compara o nome do usuário da reserva com o usuário autenticado
            if (booking.customerName().equals(currentUser)) {
                Booking cancelledBooking = new Booking(
                        booking.id(),
                        booking.customerName(),
                        booking.destination(),
                        booking.startDate(),
                        booking.endDate(),
                        BookingStatus.CANCELLED, // O novo status
                        booking.category()
                );

                this.bookings.replace(bookingId, cancelledBooking);
                return Optional.of(cancelledBooking);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Booking> findPackagesByCategory(Category category) {
        return bookings.values().stream()
                .filter(booking -> category.equals(booking.category()))
                .toList();
    }
}