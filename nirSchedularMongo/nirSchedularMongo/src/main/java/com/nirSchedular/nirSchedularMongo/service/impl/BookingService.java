package com.nirSchedular.nirSchedularMongo.service.impl;

import com.nirSchedular.nirSchedularMongo.dto.BookingDTO;
import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.Appointment;
import com.nirSchedular.nirSchedularMongo.entity.Booking;
import com.nirSchedular.nirSchedularMongo.entity.User;
import com.nirSchedular.nirSchedularMongo.exception.OurException;
import com.nirSchedular.nirSchedularMongo.repo.AppointmentRepository;
import com.nirSchedular.nirSchedularMongo.repo.BookingRepository;
import com.nirSchedular.nirSchedularMongo.repo.UserRepository;
import com.nirSchedular.nirSchedularMongo.service.interfac.IAppointmentService;
import com.nirSchedular.nirSchedularMongo.service.interfac.IBookingService;
import com.nirSchedular.nirSchedularMongo.service.interfac.IEmailService;
import com.nirSchedular.nirSchedularMongo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final IEmailService emailService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          AppointmentRepository appointmentRepository,
                          UserRepository userRepository,
                          IEmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public Response saveBooking(String appointmentId, String userId, Booking bookingRequest) {
        Response response = new Response(); // Initializes the response object

        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);

            bookingRequest.setAppointment(appointment);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

            Booking savedBooking = bookingRepository.save(bookingRequest);

            List<Booking> userBookings = user.getBookings() != null ? user.getBookings() : new ArrayList<>();
            userBookings.add(savedBooking);
            user.setBookings(userBookings);
            userRepository.save(user);

            appointment.setBooked(true); // just mark as booked
            appointmentRepository.save(appointment);

            String subject = "אישור תאום פגישת הדרכה - ניר הדרכת עבודה בגובה";

            String body = "היי " + user.getName() + ",\n\n" +
                    "ההזמנה שלך אושרה. הנה הפרטים:\n" +
                    "קוד אישור הזמנה: " + bookingConfirmationCode + "\n" +
                    "כתובת אימייל לאישור: " + appointment.getUserEmail() + "\n" +
                    "תאריך הפגישה: " + bookingRequest.getDate() + "\n" +
                    "מועד: " + appointment.getTimeSlot() + "\n" +
                    "מחכים לראותכם.\n\n" +
                    "ניר הדרכת עבודה בגובה";

            emailService.sendEmail(user.getEmail(), subject, body);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a booking " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response(); // Initializes the response object

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedAppointments(booking, true);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response(); // Initializes the response object

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("Getting all bookings was successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(String bookingId) {
        Response response = new Response(); // Initializes the response object

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Not Found"));

            User user = booking.getUser();
            if (user != null) {
                user.getBookings().removeIf(b -> b.getId().equals(bookingId));
                userRepository.save(user);
            }

            Appointment appointment = booking.getAppointment();
            if (appointment != null) {
                appointment.setBooked(false);
                appointmentRepository.save(appointment);
            }

            bookingRepository.deleteById(bookingId);

            response.setMessage("Booking canceled successfully");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling a booking " + e.getMessage());
        }
        return response;
    }
}
