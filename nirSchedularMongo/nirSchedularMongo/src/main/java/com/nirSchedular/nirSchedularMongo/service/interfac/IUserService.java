package com.nirSchedular.nirSchedularMongo.service.interfac;

import com.nirSchedular.nirSchedularMongo.dto.LoginRequest;
import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.entity.User;

/**
 * This interface defines the contract for user-related operations
 * such as registration, authentication, data retrieval, and deletion.
 */
public interface IUserService {

    /**
     * Registers a new user in the system.
     *
     * @param user The user entity to be registered.
     * @return Response indicating success or failure of registration.
     */
    Response register(User user);

    /**
     * Logs a user into the system using provided login credentials.
     *
     * @param loginRequest Contains email and password fields.
     * @return Response indicating success or failure of authentication.
     */
    Response login(LoginRequest loginRequest);

    /**
     * Retrieves all registered users in the system.
     *
     * @return Response containing a list of all users.
     */
    Response getAllUsers();

    /**
     * Fetches the booking history for a specific user.
     *
     * @param userId The ID of the user whose bookings are to be retrieved.
     * @return Response with the user's booking history.
     */
    Response getUserBookingHistory(String userId);

    /**
     * Deletes a user from the system.
     *
     * @param userId The ID of the user to be deleted.
     * @return Response indicating the result of the deletion.
     */
    Response deleteUser(String userId);

    /**
     * Retrieves detailed user information by user ID.
     *
     * @param userId The ID of the user to be fetched.
     * @return Response containing the user's data.
     */
    Response getUserById(String userId);

    /**
     * Retrieves user information based on email address.
     * Typically used to fetch the currently logged-in user's info.
     *
     * @param email The email of the user.
     * @return Response containing user information.
     */
    Response getMyInfo(String email);
}
