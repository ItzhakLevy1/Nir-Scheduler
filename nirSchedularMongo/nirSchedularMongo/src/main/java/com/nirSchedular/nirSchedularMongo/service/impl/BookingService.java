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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Response saveBooking(String appointmentId, String userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            // 1. Fetch the appointment by ID
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment not found"));

            // 2. Check if already booked
            validateAppointmentNotBooked(appointment);

            // 3. Fetch user by ID
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            // 4. Generate a unique booking confirmation code
            String bookingConfirmationCode = generateUniqueConfirmationCode();

            // 5. Set associations and metadata
            bookingRequest.setAppointment(appointment);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

            // 6. Save booking in DB
            Booking savedBooking = bookingRepository.save(bookingRequest);

            // 7. Link booking to user and persist
            linkBookingToUser(user, savedBooking);

            // 8. Mark appointment as booked
            markAppointmentAsBooked(appointment);

            // 9. Send email confirmation
            sendBookingConfirmationEmail(user, appointment, bookingRequest, bookingConfirmationCode);

            // 10. Prepare response
            response.setStatusCode(200);
            response.setMessage("Booking successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a booking: " + e.getMessage());
        }

        return response;
    }


    private void validateAppointmentNotBooked(Appointment appointment) {
        if (appointment.isBooked()) {
            throw new OurException("Appointment is already booked");
        }
    }

    private String generateUniqueConfirmationCode() {
        String code;
        do {
            code = Utils.generateRandomConfirmationCode(10);
        } while (bookingRepository.findByBookingConfirmationCode(code).isPresent());
        return code;
    }

    private void linkBookingToUser(User user, Booking booking) {
        List<Booking> bookings = user.getBookings() != null ? user.getBookings() : new ArrayList<>();
        bookings.add(booking);
        user.setBookings(bookings);
        userRepository.save(user);
    }

    private void markAppointmentAsBooked(Appointment appointment) {
        appointment.setBooked(true);
        appointmentRepository.save(appointment);
    }

    private void sendBookingConfirmationEmail(User user, Appointment appointment, Booking booking, String code) {
        String subject = "אישור תאום פגישת הדרכה - ניר הדרכת עבודה בגובה";
        String body = "היי " + user.getName() + ",\n\n" +
                "ההזמנה שלך אושרה. הנה הפרטים:\n" +
                "קוד אישור הזמנה: " + code + "\n" +
                "כתובת אימייל לאישור: " + appointment.getUserEmail() + "\n" +
                "תאריך הפגישה: " + booking.getDate() + "\n" +
                "מועד: " + appointment.getTimeSlot() + "\n" +
                "מחכים לראותכם.\n\n" +
                "ניר הדרכת עבודה בגובה";

        emailService.sendEmail(user.getEmail(), subject, body);
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
