package com.nirSchedular.nirSchedularMongo.service.interfac;

import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Booking;

public interface IBookingService {

    Response saveBooking(String appointmentId, String userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(String bookingId);
}
