package com.nirSchedular.nirSchedularMongo.repo;

import com.nirSchedular.nirSchedularMongo.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode); // Custom method to find a booking by its confirmation code

    List<Booking> findBookingsByDate(LocalDate date); // Custom method to find bookings by date

    // Add a custom query to find all available bookings so that we could show them in the UI in a different color than the booked ones
    @Query("{ 'isAvailable' : true }") // Query to find all available bookings
    List<Booking> findAllAvailableBookings(); // Custom method to find all available bookings


}
