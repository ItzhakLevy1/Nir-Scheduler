package com.nirSchedular.nirSchedularMongo.repo;

import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    @Aggregation(" {$group: { _id: '$appointmentType' }}") // Aggregation to group by appointment type
    List<String> findDistinctAppointmentType(); // Custom method to find distinct appointment types incase of multiple appointment types in the same collection

    @Query("{ 'bookings' : {$size: 0 }}") // Query to find all available appointments, could have also be written like this : @Query("{ 'isAvailable' : true }")
    List<Appointment> findAllAvailableAppointments(); // Custom method to find all available appointments

    List<Appointment> findByTimeSlotAndIdNotIn(String timeSlot, List<String> bookedAppointmentIds); // Custom method to find appointments by type and exclude booked ones

    List<Appointment> findByDateAndBookedFalse(LocalDate date); // Custom method to find all available appointments for a specific date

    List<Appointment> findByBookedTrue();   // Custom method to find all booked appointments

    boolean existsByDateAndTimeSlotAndBookedTrue(LocalDate date, String timeSlot);  // Custom method to check if an appointment is booked for a specific date and time slot

    /**
     * Find an appointment by its confirmation code (typically used to verify booking).
     */
    Optional<Appointment> findByConfirmationCode(String confirmationCode);

}


/*
   This interface extends MongoRepository, which provides CRUD operations for the Appointment entity
   The first parameter is the entity type (Appointment), and the second is the type of the entity's ID (String)
   No additional methods are defined here, but you can add custom query methods if needed
   For example, you could add a method to find appointments by a specific field, like date or user
   Example: List<Appointment> findByDate(Date date);
   You can also use Spring Data's query derivation feature to create methods based on the naming convention
   For example, if you have a field 'date' in the Appointment entity, you could create a method like:
   List<Appointment> findByDate(Date date);
 */