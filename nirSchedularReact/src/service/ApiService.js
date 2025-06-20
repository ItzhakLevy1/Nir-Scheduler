import axios from "axios";

export default class ApiService {
  static BASE_URL = "http://localhost:4040"; // Defines the base URL of the API server. All the HTTP requests will use this as the root of their paths

  // Function to get the authorization header
  static getHeader() {
    const token = localStorage.getItem("token");
    console.log("Authorization token:", token);
    return {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };
  }

  // Method to refresh the token
  static async refreshToken() {
    try {
      console.log("Attempting to refresh token...");
      const refreshToken = localStorage.getItem("refreshToken");
      console.log("Refresh token:", refreshToken);
      const response = await axios.post(`${this.BASE_URL}/auth/refresh-token`, {
        token: refreshToken,
      });
      localStorage.setItem("token", response.data.token);
      console.log("Token refreshed successfully");
      return response.data.token;
    } catch (error) {
      console.error("Failed to refresh token:", error.message);
      throw new Error("Failed to refresh token: " + error.message);
    }
  }

  /******************* AUTH *******************/

  /* This  register a new user */
  static async registerUser(registration) {
    // This method sends a POST request to the /auth/register endpoint with the user registration details,
    // The registration parameter contains the user information
    const response = await axios.post(
      `${this.BASE_URL}/auth/register`,
      registration
    );
    return response.data; // It returns the data received from the API, which may be a success message or the registered user object
  }

  /* This  login a registered user */
  static async loginUser(loginDetails) {
    // This method sends a POST request to the /auth/login endpoint, with the login credentials (loginDetails)
    const response = await axios.post(
      `${this.BASE_URL}/auth/login`,
      loginDetails
    );

    return response.data; // Upon successful login, the server will usually return a token (e.g., JWT) that can be stored in localStorage
  }

  /******************* USERS *******************/

  /*  This is to get all of the users profiles */
  static async getAllUsers() {
    const response = await axios.get(`${this.BASE_URL}/users/all`, {
      // This method sends a GET request to /users/all to retrieve a list of all users
      headers: this.getHeader(), // It includes authentication headers by calling this.getHeader() to ensure the user has the correct authorization to perform this action
    });
    return response.data; // It returns the data received from the API
  }

  static async getUserProfile() {
    // This method retrieves the profile information of the currently logged-in user
    const response = await axios.get(
      `${this.BASE_URL}/users/get-logged-in-profile-info`,
      {
        headers: this.getHeader(), // The API knows which user to return because of the Authorization token provided in the request headers
      }
    );
    return response.data; // It returns the data received from the API
  }

  /* This is to get a single specific user */
  static async getUser(userId) {
    const response = await axios.get(
      `${this.BASE_URL}/users/get-by-id/${userId}`,
      {
        // This method retrieves a single user by their userId.
        headers: this.getHeader(), // It includes authentication headers by calling this.getHeader() to ensure the user has the correct authorization to perform this action
      }
    );
    return response.data; // It returns the data received from the API
  }

  /* This is to get user bookings by the user id */
  static async getUserBookings(userId) {
    // This method is designed to fetch all bookings made by a specific user, identified by their userId
    const response = await axios.get(
      `${this.BASE_URL}/users/get-user-bookings/${userId}`,
      {
        // The function accepts userId as a parameter,
        // which will be used in the API endpoint to specify
        // which user's bookings to retrieve
        headers: this.getHeader(), // It includes authentication headers by calling this.getHeader() to ensure the user has the correct authorization to perform this action
      }
    );
    return response.data; // It returns the data received from the API
  }

  /* This is to delete a user */
  static async deleteUser(userId) {
    // Takes a String user ID as a parameter and returns a Response object
    const response = await axios.delete(
      `${this.BASE_URL}/users/delete/${userId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data; // It returns the data received from the API
  }

  // Function to update user profile
  static async updateUserProfile(userData) {
    try {
      console.log("Attempting to update user profile with data:", userData);
      const response = await axios.put(
        `${this.BASE_URL}/users/update-profile`,
        userData,
        {
          headers: this.getHeader(),
        }
      );
      if (response.status === 200) {
        console.log(
          "User profile updated successfully:",
          response.data.message
        );
        // Re-authenticate the user with the updated details
        const loginResponse = await this.loginUser({
          email: userData.email,
          password: userData.password, // Assuming the password is part of userData
        });
        localStorage.setItem("token", loginResponse.token);
        return response.data.user; // Return the updated user data
      } else {
        throw new Error("Failed to update profile: Unexpected response status");
      }
    } catch (error) {
      console.error("Failed to update profile:", error.message);
      if (error.response && error.response.status === 403) {
        try {
          console.log("Access forbidden, attempting to refresh token...");
          await this.refreshToken();
          const retryResponse = await axios.put(
            `${this.BASE_URL}/users/update-profile`,
            userData,
            {
              headers: this.getHeader(),
            }
          );
          if (retryResponse.status === 200) {
            console.log(
              "User profile updated successfully after token refresh:",
              retryResponse.data.message
            );
            // Re-authenticate the user with the updated details
            const loginResponse = await this.loginUser({
              email: userData.email,
              password: userData.password, // Assuming the password is part of userData
            });
            localStorage.setItem("token", loginResponse.token);
            return retryResponse.data.user; // Return the updated user data
          } else {
            throw new Error(
              "Failed to update profile after token refresh: Unexpected response status"
            );
          }
        } catch (retryError) {
          console.error(
            "Failed to update profile after token refresh:",
            retryError.message
          );
          throw new Error(
            "Failed to update profile: Access forbidden. Please check your permissions."
          );
        }
      }
      throw new Error("Failed to update profile: " + error.message);
    }
  }

  /******************* APPOINTMENT *******************/

  /* This adds a new appointment to the database */
  static async addAppointment(formData) {
    const result = await axios.post(
      `${this.BASE_URL}/appointments/add`,
      formData,
      {
        // The request is authenticated, so it includes headers for authorization.
        headers: this.getHeader(), // It includes authentication headers by calling this.getHeader() to ensure the user has the correct authorization to perform this action
      }
    );
    return result.data; // It returns the data received from the API
  }

  /* This  gets all availavle appointments */
  static async getAllAvailableAppointments() {
    // Retrieves all available rooms without requiring authentication headers.
    const result = await axios.get(
      `${this.BASE_URL}/appointments/all-available-appointments`
    );
    return result.data; // It returns the data received from the API
  }

  /* This gets all availavle by dates appointments from the database with a given date and a time slot */
  static async getAvailableAppointmentsByDateAndTimeSlot(date, timeSlot) {
    const result = await axios.get(
      // Retrieves available appointments filtered by date, timeSlot via query parameters
      `${this.BASE_URL}/appointments/available-appointments-by-date-and-timeSlot?date=${date}&timeSlot=${timeSlot}`
    );
    return result.data; // It returns the data received from the API
  }

  /* This function gets a room by the id */
  static async getAppointmentById(appointmentId) {
    const result = await axios.get(
      `${this.BASE_URL}/appointments/appointment-by-id/${appointmentId}`
    );
    return result.data;
  }

  /* This  deletes a appointments by the Id */
  static async deleteAppointments(appointmentId) {
    const result = await axios.delete(
      `${this.BASE_URL}/appointments/delete/${appointmentId}`,
      {
        headers: this.getHeader(),
      }
    );
    return result.data;
  }

  /* This updates a appointment */
  static async updateAppointment(appointmentId, formData) {
    const result = await axios.put(
      `${this.BASE_URL}/appointments/update/${appointmentId}`,
      formData,
      {
        // Updates appointment information by sending a PUT request with appointment data in formData
        headers: {
          ...this.getHeader(), // Updates room information by sending a PUT request with room data in formData
          "Content-Type": "multipart/form-data", // The Content-Type here is multipart/form-data, which allows you to send files (like images) in the request body
        },
      }
    );
    return result.data;
  }

  /******************* BOOKING *******************/

  /* This saves a new booking to the databse */
  static async bookAppointment(userId, appointmentData) {
    console.log("API CALL → bookAppointment");
    console.log("URL:", `${this.BASE_URL}/appointments/book/${userId}`);
    console.log("Payload (appointmentData):", appointmentData);
    console.log("Headers:", this.getHeader());

    return axios.post(
      `${this.BASE_URL}/appointments/book/${userId}`,
      appointmentData,
      { headers: this.getHeader() }
    );
  }

  /* This gets all bookings from the database */
  static async getAllappointments() {
    const result = await axios.get(`${this.BASE_URL}/admin/manage-bookings`, {
      headers: this.getHeader(),
    });
    return result.data;
  }

  /* This gets a booking by the confirmation code */
  static async getBookingByConfirmationCode(bookingCode) {
    const result = await axios.get(
      `${this.BASE_URL}/bookings/get-by-confirmation-code/${bookingCode}`
    );
    return result.data;
  }

  /* This is the to cancel user booking */
  static async cancelBooking(bookingId) {
    const result = await axios.delete(
      `${this.BASE_URL}/bookings/cancel/${bookingId}`,
      {
        headers: this.getHeader(),
      }
    );
    return result.data;
  }

  /******************* AUTHENTICATION CHECKER *******************/

  static logout() {
    // Removes the token and role from localStorage, effectively logging out the user
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  static isAuthenticated() {
    // Checks if the user is authenticated by seeing if a token exists in localStorage
    const token = localStorage.getItem("token");
    return !!token;
  }

  static isAdmin() {
    // check the user's role by reading the role from localStorage
    const role = localStorage.getItem("role");
    return role === "ADMIN";
  }

  static isUser() {
    // check the user's role by reading the role from localStorage
    const role = localStorage.getItem("role");
    return role === "USER";
  }
}
// export default new ApiService();

/*
Summary:

Purpose: This ApiService class centralizes all the API calls needed for your application, which is a great design pattern for organizing HTTP requests in a single place.
Authentication: Methods use a token from localStorage to ensure secure communication.
CRUD Operations: It provides various create, read, update, and delete operations for users, appointments, and bookings.
Modular: Each method performs one task, making the class easy to maintain and extend in the future.
*/
