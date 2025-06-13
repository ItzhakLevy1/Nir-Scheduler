import React, { useState } from "react";
import ApiService from "../../service/ApiService"; // Service to handle API requests
import { useNavigate } from "react-router-dom"; // React Router hook for navigation
import "./auth.css";

function RegisterPage() {
  const navigate = useNavigate(); // Hook to programmatically navigate to other routes

  // State to manage form input values
  const [formData, setFormData] = useState({
    name: "", // User's name
    email: "", // User's email
    password: "", // User's password
    phoneNumber: "", // User's phone number
  });

  // State for error and success messages
  const [errorMessage, setErrorMessage] = useState(""); // Error message for validation or server issues
  const [successMessage, setSuccessMessage] = useState(""); // Success message upon successful registration

  // Handles input changes for the form
  const handleInputChange = (e) => {
    const { name, value } = e.target; // Get the name and value of the input field
    setFormData({ ...formData, [name]: value }); // Update the corresponding field in the formData state
  };

  // Validates the phone number format
  const isValidPhoneNumber = (phoneNumber) => {
    // Example regex for a valid phone number
    const regex =
      /^[+]?(\d{1,4}[-.\s]?)?(\(?\d{1,4}\)?[-.\s]?)?\d{1,4}[-.\s]?\d{1,4}[-.\s]?\d{1,4}$/;
    return regex.test(phoneNumber);
  };

  // Validates the form to ensure all fields are filled and phone number is valid
  const validateForm = () => {
    const { name, email, password, phoneNumber } = formData;
    if (!name || !email || !password || !phoneNumber) {
      setErrorMessage("אנא מלא את כל השדות.");
      return false;
    }
    if (!isValidPhoneNumber(phoneNumber)) {
      setErrorMessage("אנא הזן מספר טלפון תקין.");
      return false;
    }

    return true; // Return true if all fields are valid
  };

  // Handles form submission for user registration
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent default form submission (e.g., page reload)

    // Validate the form
    if (!validateForm()) {
      setTimeout(() => setErrorMessage(""), 5000); // Clear error after 5 seconds
      return;
    }
    try {
      // Call the `registerUser` method from ApiService to send form data to the server
      const response = await ApiService.registerUser(formData);
      // Check if the registration is successful
      if (response.statusCode === 200) {
        // Clear form fields
        setFormData({
          name: "",
          email: "",
          password: "",
          phoneNumber: "",
        });
        setSuccessMessage("המשתמש נרשם בהצלחה!");
        setTimeout(() => {
          setSuccessMessage("");
          navigate("/login"); // Navigate to the login page
        }, 3000);
      }
    } catch (error) {
      // Check for specific error like "Email is already taken"
      if (
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage(error.message || "אירעה שגיאה");
      }
      setTimeout(() => setErrorMessage(""), 5000); // Clear error after 5 seconds
    }
  };

  return (
    <div className="auth-container">
      {/* Show error message, if any */}
      {errorMessage && <p className="error-message">{errorMessage}</p>}
      {/* Show success message, if any */}
      {successMessage && <p className="success-message">{successMessage}</p>}
      <h2>הרשמה</h2>
      {/* Registration form */}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>שם מלא:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <label>אימייל:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <label>מספר טלפון:</label>
          <input
            type="text"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <label>סיסמה:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            required
          />
        </div>
        <button type="submit">הרשם</button>
      </form>
      <p className="register-link">
        כבר יש לך חשבון? <a href="/login">להתחברות</a>
      </p>
    </div>
  );
}

export default RegisterPage;
