import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom"; // React Router hooks for navigation and location
import ApiService from "../../service/ApiService"; // API service for server communication
import Spinner from "react-bootstrap/Spinner"; // Import Spinner from react-bootstrap
import "bootstrap/dist/css/bootstrap.min.css";

function LoginPage() {
  // State variables to store form inputs and errors
  const [email, setEmail] = useState(""); // User's email address
  const [password, setPassword] = useState(""); // User's password
  const [error, setError] = useState(""); // Error message for login issues
  const [loading, setLoading] = useState(false); // Loading state for spinner

  // React Router hooks for navigation and access to the current location
  const navigate = useNavigate(); // To programmatically navigate to other routes
  const location = useLocation(); // To access the current route's location details

  // Determines where to navigate after login (fallback to "/home" if no previous page is specified)
  const from = location.state?.from?.pathname || "/";

  // Handles form submission for login
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent the default form submission behavior (e.g., page reload)

    // Ensure both email and password are provided
    if (!email || !password) {
      setError("Please fill in all fields."); // Show an error if fields are empty
      setTimeout(() => setError(""), 5000); // Clear the error after 5 seconds
      return;
    }

    setLoading(true); // Set loading state to true before making the request

    try {
      // Call the API to log in the user
      const response = await ApiService.loginUser({ email, password });

      // If the response is successful, store user details and navigate
      if (response.statusCode === 200) {
        localStorage.setItem("token", response.token); // Save the user's JWT token
        localStorage.setItem("role", response.role); // Save the user's role (e.g., admin, user, etc.)
        console.log("response : ", response);

        // Fetch user profile to get userId and name
        try {
          const profileResponse = await ApiService.getUserProfile();
          console.log("profileResponse : ", profileResponse);
          if (
            profileResponse &&
            profileResponse.user &&
            profileResponse.user.id
          ) {
            localStorage.setItem("userId", profileResponse.user.id);
            // Store user name for header display
            localStorage.setItem(
              "userProfile",
              JSON.stringify({ name: profileResponse.user.name })
            );
          }
        } catch (profileError) {
          console.error(
            "Failed to fetch user profile for userId:",
            profileError
          );
        }
        // Log stored values for verification
        const storedToken = localStorage.getItem("token");
        const storedRole = localStorage.getItem("role");
        const storedUserId = localStorage.getItem("userId");
        console.log("[Login] Stored token:", storedToken);
        console.log("[Login] Stored role:", storedRole);
        console.log("[Login] Stored userId:", storedUserId);
        window.dispatchEvent(new Event("nir-auth-change")); // A custom event to notify other components of login/logout actions (auth change) 
        navigate(from, { replace: true }); // Redirect to the page the user came from or fallback to "/home"
      }
    } catch (error) {
      // Handle any errors from the login request
      setError(error.response?.data?.message || error.message); // Show the server's error message or a generic one
      setTimeout(() => setError(""), 5000); // Clear the error after 5 seconds
    } finally {
      setLoading(false); // Set loading state to false after the request is complete
    }
  };

  return (
    <div className="auth-container">
      <h2>התחברות</h2>

      {/* Display error messages, if any */}
      {error && <p className="error-message">{error}</p>}

      {/* Conditionally render the spinner or the login form */}
      {loading ? (
        <Spinner animation="border" role="status">
          <span className="visually-hidden">טוען...</span>
        </Spinner>
      ) : (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>אימייל: </label>
            <input
              type="email" // Input type is email for validation
              value={email} // Binds input value to the email state
              onChange={(e) => setEmail(e.target.value)} // Updates state when the user types
              required // Ensures the field is required
            />
          </div>
          <div className="form-group">
            <label>סיסמה: </label>
            <input
              type="password" // Input type is password for masking characters
              value={password} // Binds input value to the password state
              onChange={(e) => setPassword(e.target.value)} // Updates state when the user types
              required // Ensures the field is required
            />
          </div>
          <button type="submit">התחבר</button>{" "}
          {/* Triggers the handleSubmit function */}
        </form>
      )}

      {/* Link to the registration page for users without an account */}
      <p className="register-link">
        אין לך חשבון? <a href="/register">להרשמה</a>
      </p>
    </div>
  );
}

export default LoginPage;
