import React, { useEffect, useState } from "react";
import axios from "axios";
import DatePicker, { registerLocale } from "react-datepicker";
import { he } from "date-fns/locale";
import { format } from "date-fns";
import "react-datepicker/dist/react-datepicker.css";
import toastr from "toastr";
import "./BookingForm.css";
import Spinner from "react-bootstrap/Spinner"; // Import Spinner from react-bootstrap
import "bootstrap/dist/css/bootstrap.min.css";

// Register Hebrew locale for the date picker
registerLocale("he", he);

/*
  This component allows users to book training sessions by selecting a date and time slot.
  It fetches booked slots from the server, displays available options, and handles form submission.
*/

const BookingForm = ({ userId }) => {
  // State to store the selected date and time slot
  const [formData, setFormData] = useState({ date: null, timeSlot: "" });

  // Map of booked time slots for each date (format: { "yyyy-MM-dd": ["morning", "evening"] })
  const [bookedMap, setBookedMap] = useState({});

  // List of time slots available for the selected date
  const [availableTimeSlots, setAvailableTimeSlots] = useState([]);

  // Loading state for spinner
  const [loading, setLoading] = useState(false);

  // Load JWT token from localStorage
  const token = localStorage.getItem("token");

  // Fetch all booked dates and time slots from the server when component mounts
  useEffect(() => {
    axios
      .get("http://localhost:4040/appointments/booked-slots", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setBookedMap(res.data); // Store booked slots data based on the data extracted from the server response
      })
      .catch((err) => {
        console.error("Error fetching booked slots", err);
      });
  }, []);

  // Called when a user selects a date
  const handleDateChange = (selectedDate) => {
    // selectedDate is a Date object that the user picks in the date picker

    // Reset selected time slot when changing the date to prevent mismatches from previous selections of time slots
    setFormData({ date: selectedDate, timeSlot: "" });

    const dateAsKey = format(selectedDate, "yyyy-MM-dd"); // Format date to "yyyy-MM-dd" for consistent keying for bookedMap
    const bookedSlots = bookedMap[dateAsKey] || []; // Get already booked slots for this date
    const allSlots = ["morning", "evening"];
    const available = allSlots.filter((slot) => !bookedSlots.includes(slot)); // Filter out booked ones
    setAvailableTimeSlots(available); // Update available options
  };

  // Check if a date should be disabled (both slots booked)
  const isDateDisabled = (date) => {
    // date is a Date object that the user picks in the date picker
    const dateAsKey = format(date, "yyyy-MM-dd"); // Format date to "yyyy-MM-dd" for consistent keying for bookedMap
    const slots = bookedMap[dateAsKey]; // Get booked slots for this date
    return Array.isArray(slots) && slots.length === 2; // If both morning & evening are booked
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();

    // Ensure both date and time slot are selected
    if (!formData.date || !formData.timeSlot) {
      alert("בחר תאריך ומשבצת זמן זמינים");
      return;
    }

    setLoading(true); // Show spinner

    // Prepare data to send to server
    const payload = {
      ...formData,
      date: format(formData.date, "yyyy-MM-dd"), // Format date
      userId,
    };

    // Send booking request to backend
    axios
      .post(`http://localhost:4040/appointments/book/${userId}`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(() => {
        toastr.success("ההזמנה בוצעה בהצלחה!");

        // Refresh booked slots from server after successful booking
        axios
          .get("http://localhost:4040/appointments/booked-slots", {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then((res) => {
            setBookedMap(res.data); // Update the state with latest booked slots
            setFormData({ date: null, timeSlot: "" }); // Reset form fields
            setAvailableTimeSlots([]); // Clear time slot options
          });
      })
      .catch((err) => {
        alert("שגיאה בעת שליחת ההזמנה.");
        console.error(err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      {loading ? (
        <div
          className="d-flex justify-content-center align-items-center spinner-container"
          style={{ minHeight: "150px" }}
        >
          <Spinner animation="border" role="status" className="big-spinner" />
        </div>
      ) : (
        <div className="booking-form">
          <h2>הזמנת הדרכה</h2>

          {/* Date picker input */}
          <label>תאריך:</label>
          <DatePicker
            selected={formData.date} // Selected date for the date picker
            onChange={handleDateChange}
            dateFormat="dd/MM/yyyy"
            locale="he"
            minDate={new Date()}
            filterDate={(date) => !isDateDisabled(date)} // Disable fully booked dates
            placeholderText="בחר תאריך פנוי"
            className="date-picker"
          />

          {/* Time slot dropdown */}
          <label>משבצת זמן:</label>
          <select
            name="timeSlot"
            value={formData.timeSlot} // Selected time slot for the dropdown
            onChange={
              (e) => setFormData({ ...formData, timeSlot: e.target.value }) // Update time slot when user selects from dropdown
            }
            required
          >
            <option value="">בחר משבצת זמן</option>
            {availableTimeSlots.map(
              (
                slot // Map through available time slots and create options
              ) => (
                <option key={slot} value={slot}>
                  {slot === "morning" ? "בוקר" : "ערב"}
                </option>
              )
            )}
          </select>

          {/* Submit button */}
          <button type="submit">שלח</button>
        </div>
      )}
    </form>
  );
};

export default BookingForm;
