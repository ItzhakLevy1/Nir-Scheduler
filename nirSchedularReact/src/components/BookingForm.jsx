import React, { useState } from "react";
import "./BookingForm.css";
import handleSubmit from "./utils/formHandlers"; // Function that handles form submission logic

// BookingForm component receives userId as a prop
const BookingForm = ({ userId }) => {
  // State to hold form input values
  const [formData, setFormData] = useState({
    fullName: "", // Full name input
    email: "", // Email input
    phone: "", // Phone number input
    date: "", // Appointment date
    timeSlot: "", // Time slot (morning/evening)
  });

  // Handles changes in input fields and updates formData state
  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev, // Preserve existing form data
      [e.target.name]: e.target.value, // Update the changed field
    }));
  };

  // Handles form submission
  const onSubmit = (e) => {
    e.preventDefault(); // Prevent default form behavior (page reload)
    handleSubmit({ ...formData, userId }); // Call external submit handler with form data + user ID
  };

  return (
    <form className="booking-form" onSubmit={onSubmit}>
      <h2>טופס הזמנת תור</h2>

      {/* Full Name Field */}
      <label>שם מלא:</label>
      <input
        type="text"
        name="fullName"
        value={formData.fullName}
        onChange={handleChange}
        required
      />

      {/* Email Field */}
      <label>אימייל:</label>
      <input
        type="email"
        name="email"
        value={formData.email}
        onChange={handleChange}
        required
      />

      {/* Phone Field */}
      <label>טלפון:</label>
      <input
        type="tel"
        name="phone"
        value={formData.phone}
        onChange={handleChange}
        required
      />

      {/* Date Field */}
      <label>תאריך:</label>
      <input
        type="date"
        name="date"
        value={formData.date}
        onChange={handleChange}
        required
      />

      {/* Time Slot Dropdown */}
      <label>משבצת זמן:</label>
      <select
        name="timeSlot"
        value={formData.timeSlot}
        onChange={handleChange}
        required
      >
        <option value="">בחר משבצת</option>
        <option value="morning">בוקר</option>
        <option value="evening">ערב</option>
      </select>

      {/* Submit Button */}
      <button type="submit">שלח</button>
    </form>
  );
};

export default BookingForm;
