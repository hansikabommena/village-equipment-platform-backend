package com.villageshare.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villageshare.backend.entity.Booking;
import com.villageshare.backend.entity.Resource;
import com.villageshare.backend.repository.BookingRepository;
import com.villageshare.backend.repository.ResourceRepository;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;

    public BookingController(
            BookingRepository bookingRepository,
            ResourceRepository resourceRepository) {

        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {

        // Check overlapping booking
        boolean alreadyBooked =
                bookingRepository.existsOverlappingBooking(
                        booking.getResourceName(),
                        booking.getStartDate(),
                        booking.getEndDate()
                );

        if (alreadyBooked) {

            throw new RuntimeException(
                    "This resource is already booked for these dates."
            );
        }

        // Find resource using resource ID
        Resource resource = null;

        if (booking.getResourceId() != null) {

            resource = resourceRepository
                    .findById(booking.getResourceId())
                    .orElse(null);
        }

        // If resource ID is not available,
        // find resource using resource name
        if (resource == null &&
                booking.getResourceName() != null) {

            resource = resourceRepository
                    .findByNameIgnoreCase(
                            booking.getResourceName()
                    );
        }

        // Set owner email from resource
        if (resource != null) {

            booking.setOwnerEmail(
                    resource.getOwnerEmail()
            );
        }

        // New booking status
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    // =========================
    // GET ALL BOOKINGS
    // =========================

    @GetMapping
    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    // =========================
    // GET BOOKINGS OF USER
    // =========================

    @GetMapping("/user/{name}")
    public List<Booking> getBookingsByUser(
            @PathVariable String name) {

        return bookingRepository.findByName(name);
    }

    // =========================
    // GET BOOKINGS OF OWNER
    // =========================

    @GetMapping("/owner/{email}")
    public List<Booking> getBookingsByOwner(
            @PathVariable String email) {

        return bookingRepository.findByOwnerEmail(email);
    }

    // =========================
    // APPROVE BOOKING
    // =========================

    @PutMapping("/{id}/approve")
    public Booking approveBooking(
            @PathVariable Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        booking.setStatus("APPROVED");

        return bookingRepository.save(booking);
    }

    // =========================
    // REJECT BOOKING
    // =========================

    @PutMapping("/{id}/reject")
    public Booking rejectBooking(
            @PathVariable Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        booking.setStatus("REJECTED");

        return bookingRepository.save(booking);
    }

    // =========================
    // CANCEL BOOKING BY USER
    // =========================

    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(
            @PathVariable Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        // Only PENDING bookings can be cancelled
        if (!booking.getStatus().equals("PENDING")) {

            throw new RuntimeException(
                    "Only pending bookings can be cancelled."
            );
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    // =========================
    // MARK RESOURCE AS RETURNED
    // =========================

    @PutMapping("/{id}/return")
    public Booking returnBooking(
            @PathVariable Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        booking.setStatus("RETURNED");

        return bookingRepository.save(booking);
    }

    // =========================
    // COMPLETE BOOKING
    // =========================

    @PutMapping("/{id}/complete")
    public Booking completeBooking(
            @PathVariable Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        booking.setStatus("COMPLETED");

        return bookingRepository.save(booking);
    }
}