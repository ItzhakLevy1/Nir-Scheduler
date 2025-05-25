package com.nirSchedular.nirSchedularMongo.utils;

import com.nirSchedular.nirSchedularMongo.dto.*;
import com.nirSchedular.nirSchedularMongo.entity.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class providing helper methods for:
 * - Generating confirmation codes
 * - Mapping entities to DTOs (and vice versa where needed)
 * - Validating phone numbers
 */
public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a random alphanumeric confirmation code of the specified length.
     *
     * @param length The desired length of the confirmation code.
     * @return A randomly generated alphanumeric string.
     */
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    /**
     * Maps a User entity to a basic UserDTO.
     *
     * @param user The User entity.
     * @return Corresponding UserDTO.
     */
    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    /**
     * Maps an Appointment entity to a basic AppointmentDTO.
     *
     * @param appointment The Appointment entity.
     * @return Corresponding AppointmentDTO.
     */
    public static AppointmentDTO mapAppointmentEntityToAppointmentDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setAppointmentType(appointment.getAppointmentType());
        appointmentDTO.setAppointmentDescription(appointment.getAppointmentDescription());
        return appointmentDTO;
    }

    /**
     * Maps a Booking entity to a basic BookingDTO.
     *
     * @param booking The Booking entity.
     * @return Corresponding BookingDTO.
     */
    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        if (booking.getAppointment() != null) {
            bookingDTO.setAppointment(mapAppointmentEntityToAppointmentDTO(booking.getAppointment()));
        }
        return bookingDTO;
    }

    /**
     * Maps an Appointment entity to an AppointmentDTO and includes its list of bookings.
     *
     * @param appointment The Appointment entity.
     * @return AppointmentDTO with nested BookingDTO list.
     */
    public static AppointmentDTO mapAppointmentEntityToAppointmentDTOPlusBookings(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setAppointmentType(appointment.getAppointmentType());
        appointmentDTO.setAppointmentDescription(appointment.getAppointmentDescription());

        if (appointment.getBookings() != null) {
            appointmentDTO.setBookings(appointment.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTO)
                    .collect(Collectors.toList()));
        }

        return appointmentDTO;
    }

    /**
     * Maps a Booking entity to a BookingDTO including nested User and Appointment info.
     *
     * @param booking The Booking entity.
     * @return BookingDTO with nested UserDTO and AppointmentDTO.
     */
    public static BookingDTO mapBookingEntityToBookingDTOPlusUser(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (booking.getUser() != null) {
            bookingDTO.setUser(mapUserEntityToUserDTO(booking.getUser()));
        } else {
            System.out.println("Booking user is null.");
        }

        if (booking.getAppointment() != null) {
            bookingDTO.setAppointment(mapAppointmentEntityToAppointmentDTO(booking.getAppointment()));
        } else {
            System.out.println("Booking appointment is null.");
        }

        return bookingDTO;
    }

    /**
     * Maps a User entity to a UserDTO including their bookings and associated appointment data.
     *
     * @param user The User entity.
     * @return UserDTO with nested BookingDTO list including appointment/user data.
     */

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedAppointments(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser) {
            // Add a null check before mapping the user
            if (booking.getUser() != null) {
                bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
            } else {
                System.out.println("Booking user is null.");
            }
        }

        if (booking.getAppointment() != null) {
            AppointmentDTO roomDTO = new AppointmentDTO();
            roomDTO.setId(booking.getAppointment().getId());
            roomDTO.setAppointmentType(booking.getAppointment().getAppointmentType());
            roomDTO.setAppointmentDescription(booking.getAppointment().getAppointmentDescription());
            bookingDTO.setAppointment(roomDTO);
        } else {
            System.out.println("Booking appointment is null.");
        }

        return bookingDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndAppointments(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTOPlusUser)
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

    /**
     * Maps a list of User entities to a list of basic UserDTOs.
     *
     * @param userList List of User entities.
     * @return List of UserDTOs.
     */
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    /**
     * Maps a list of Appointment entities to a list of basic AppointmentDTOs.
     *
     * @param appointmentList List of Appointment entities.
     * @return List of AppointmentDTOs.
     */
    public static List<AppointmentDTO> mapAppointmentListEntityToAppointmentListDTO(List<Appointment> appointmentList) {
        return appointmentList.stream().map(Utils::mapAppointmentEntityToAppointmentDTO).collect(Collectors.toList());
    }

    /**
     * Maps a list of Booking entities to a list of basic BookingDTOs.
     *
     * @param bookingList List of Booking entities.
     * @return List of BookingDTOs.
     */
    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

    /**
     * Validates a phone number against a flexible regex pattern.
     * Supports optional +, country codes, dashes, dots, and spaces.
     *
     * @param phoneNumber The phone number string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[+]?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
