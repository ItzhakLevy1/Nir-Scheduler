package com.nirSchedular.nirSchedularMongo.service.interfac;

import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;

import java.time.LocalDate;

public interface IAppointmentService {

    /**
     * Creates a new appointment document in the database.
     * This method is typically called when a user confirms their booking.
     * @param userId The ID of the user making the booking.
     * @param appointment The appointment details to book.
     * @return The booked appointment with updated status and confirmation code.
     */

    Response bookAppointment(String userId, Appointment appointment);

    /**
     * Retrieves all appointment documents.
     * @return A response containing a list of all appointments.
     */
    Response getAllAppointments();

    /**
     * Retrieves all available appointments across all dates.
     * This is useful for booking views or availability calendars.
     * @return A response containing a list of all available appointments.
     */
    Response getAllAvailableAppointments();


    /**
     * Retrieves a single appointment by its unique ID.
     * @param appointmentId The MongoDB document ID.
     * @return A response with the found appointment or an error.
     */
    Response getAppointmentById(String appointmentId);

    /**
     * Updates an existing appointment.
     * @param appointmentId The ID of the appointment to update.
     * @return A response with the update status.
     */

    public Response updateAppointment(String appointmentId, Appointment updatedData);

    /**
     * Deletes an appointment from the database.
     * @param appointmentId The ID of the appointment to delete.
     * @return A response with the deletion result.
     */
    Response deleteAppointment(String appointmentId);


    /**
     * Removes a booking reference from an existing appointment.
     * @param appointmentId The appointment to update.
     * @param bookingId The booking ID to unlink.
     * @return A response indicating success or failure.
     */
    Response removeBookingFromAppointment(String appointmentId, String bookingId);

    /**
     * Retrieves available appointments for a given date.
     * @param date The date to filter available appointments.
     * @return A response with available appointments.
     */
    Response getAvailableAppointmentsByDate(LocalDate date);

    /**
     * Get appointment details by confirmation code.
     */
    Response getAppointmentByConfirmationCode(String confirmationCode);

}
