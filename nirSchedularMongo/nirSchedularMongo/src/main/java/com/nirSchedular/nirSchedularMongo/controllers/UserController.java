package com.nirSchedular.nirSchedularMongo.controllers;


import com.nirSchedular.nirSchedularMongo.dto.Response;
import com.nirSchedular.nirSchedularMongo.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController              // This annotation is used to mark the class as a Spring MVC controller where each method returns a ResponseEntity (typically JSON or XML). It combines @Controller and @ResponseBody
@RequestMapping("/users")    // Specifies that the base URL for all the methods in this controller will start with /users
public class UserController {
    @Autowired              // Injects the IUserService interface, which provides the methods required to interact with user-related data
    private IUserService userService;


    @GetMapping("/all")                                                         // Handles GET requests to retrieve all users from the system
    @PreAuthorize("hasAuthority('ADMIN')")                                      // Restricts access to this method to users who have the 'ADMIN' authority
    public ResponseEntity<Response> getAllUsers() {                             // Handles a request to fetch all users and returns a ResponseEntity wrapping a custom Response object (which likely includes data and HTTP status code)
        Response response = userService.getAllUsers();                          // Retrieves all users from the system and stores them in the response object
        return ResponseEntity.status(response.getStatusCode()).body(response);  // Returns a ResponseEntity object, with the HTTP status set according to the result from the userService.getAllUsers() call
    }

    @GetMapping("/get-by-id/{userId}")                                                      // Handles GET requests to retrieve a specific user by their user ID
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {    // Retrieves a user by their unique ID, passed as a URL path variable ({userId}) and returns a ResponseEntity wrapping a custom Response object (which likely includes data and HTTP status code)
        Response response = userService.getUserById(userId);                                // Returns the user's information if found
        return ResponseEntity.status(response.getStatusCode()).body(response);              // Builds and returns a ResponseEntity with the HTTP status and response body from the service
    }

    @DeleteMapping("/delete/{userId}")                                                      // Handles DELETE requests to remove a user by their user ID
    @PreAuthorize("hasAuthority('ADMIN') or #userId == authentication.principal.id")        // Only users with the 'ADMIN' authority can access this endpoint
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {     // Deletes a user by their unique ID ({userId})
        Response response = userService.deleteUser(userId);                                 // Returns a success message or an error if the deletion fails
        return ResponseEntity.status(response.getStatusCode()).body(response);              // Builds and returns a ResponseEntity with the HTTP status and response body from the service
    }

    @GetMapping("/get-logged-in-profile-info")                                                  // Handles GET requests to retrieve the currently authenticated user's profile information
    public ResponseEntity<Response> getLoggedInUserProfile() {                                  // Retrieves the profile information of the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get the current authenticated user's details, specifically their email address
        String email = authentication.getName();
        Response response = userService.getMyInfo(email);                                       // Returns the profile information of the logged-in user based on the email extracted from the authentication object
        return ResponseEntity.status(response.getStatusCode()).body(response);                  // Builds and returns a ResponseEntity with the HTTP status and response body from the service
    }

    @GetMapping("/get-user-bookings/{userId}")                                                      // Handles GET requests to fetch the booking history for a specific user
    public ResponseEntity<Response> getUserBookingHistory(@PathVariable("userId") String userId) {  // Retrieves the booking history of a user identified by their unique ID ({userId})
        Response response = userService.getUserBookingHistory(userId);                              // Returns the user's booking history or an error if something goes wrong
        return ResponseEntity.status(response.getStatusCode()).body(response);                      // Builds and returns a ResponseEntity with the HTTP status and response body from the service
    }
}


/*
Summary:
This UserController provides basic user management features while enforcing security using Spring Security's @PreAuthorize.


Suggestions / Enhancements to consider:
Global Exception Handling: As mentioned earlier, adding global exception handling using @ControllerAdvice would centralize error management.
Logging: You might want to add some logging to track important actions such as when users are fetched or deleted.
Validation: It's a good idea to add validation for input (such as user IDs and request payloads) to ensure that they meet the required format or constraints.
This UserController provides basic user management features while enforcing security using Spring Security's @PreAuthorize.
 */