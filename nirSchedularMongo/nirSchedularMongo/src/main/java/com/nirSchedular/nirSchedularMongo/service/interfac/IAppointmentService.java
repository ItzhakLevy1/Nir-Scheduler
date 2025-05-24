package com.nirSchedular.nirSchedularMongo.service.interfac;

import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;

public interface IAppointmentService {

    /**
     * Creates a new appointment document in the database.
     * @param appointment The appointment to create.
     * @return A standardized response with creation result.
     */
    Response addNewAppointment(Appointment appointment);

    /**
     * Retrieves all appointment documents.
     * @return A response containing a list of all appointments.
     */
    Response getAllAppointments();

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
    Response updateAppointment(String appointmentId);

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
}
