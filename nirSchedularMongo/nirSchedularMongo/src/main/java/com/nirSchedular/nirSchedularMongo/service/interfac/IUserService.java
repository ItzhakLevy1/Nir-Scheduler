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
     * Updates the profile of the currently logged-in user.
     *
     * This method is typically used in contexts where the system
     * already knows who the authenticated user is (e.g., from session or token),
     * and only the updated user information is passed in.
     *
     * @param updatedUser The user entity with updated profile information.
     * @return Response indicating success or failure of the profile update.
     */
    Response updateUserProfile(User updatedUser);

    /**
     * Updates the profile of a user, explicitly providing the authenticated user's email.
     *
     * This method is useful in contexts (e.g., REST APIs) where the authenticated
     * user's email must be passed separately (e.g., extracted from JWT or request context).
     * It ensures the update is applied only to the authorized user's profile.
     *
     * @param updatedUser The user entity containing the updated information.
     * @param authenticatedUserEmail The email of the currently authenticated user.
     * @return Response indicating success or failure of the profile update.
     */
    Response updateUserProfile(User updatedUser, String authenticatedUserEmail);


    boolean isEmailTaken(String email);
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
