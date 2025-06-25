import React, { useEffect, useState } from "react";
import axios from "axios";
import DatePicker, { registerLocale } from "react-datepicker";
import { he } from "date-fns/locale";
import { format } from "date-fns";
import "react-datepicker/dist/react-datepicker.css";
import "./BookingForm.css";

registerLocale("he", he); // Register Hebrew locale for DatePicker

const BookingForm = ({ userId }) => {
  // State to manage form data and available time slots
  const [formData, setFormData] = useState({
    date: null,
    timeSlot: "",
  });

  const [bookedMap, setBookedMap] = useState({}); // Map to store booked slots by date
  const [availableTimeSlots, setAvailableTimeSlots] = useState([]); // Array to store available time slots

  // Load token from localStorage
  const token = localStorage.getItem("token");

  // Fetch booked slots
  useEffect(() => {
    axios
      .get("http://localhost:4040/appointments/booked-slots", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setBookedMap(res.data); // Set the booked slots map
      })
      .catch((err) => {
        console.error("Error fetching booked slots", err);
      });
  }, []);

  // Handle date change in the DatePicker
  const handleDateChange = (selectedDate) => {  // selectedDate is a Date object
    setFormData({ date: selectedDate, timeSlot: "" });

    const key = format(selectedDate, "yyyy-MM-dd");
    const bookedSlots = bookedMap[key] || [];
    const allSlots = ["morning", "evening"];
    const available = allSlots.filter((slot) => !bookedSlots.includes(slot));
    setAvailableTimeSlots(available);
  };

  const isDateDisabled = (date) => {
    const key = format(date, "yyyy-MM-dd");
    const slots = bookedMap[key];
    return Array.isArray(slots) && slots.length === 2;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.date || !formData.timeSlot) {
      alert("בחר תאריך ומשבצת זמן זמינים");
      return;
    }

    const payload = {
      ...formData,
      date: format(formData.date, "yyyy-MM-dd"),
      userId,
    };

    axios
      .post(`http://localhost:4040/appointments/book/${userId}`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(() => {
        alert("ההזמנה בוצעה בהצלחה!");

        // 1. Get the new snapshot of booked slots
        axios
          .get("http://localhost:4040/appointments/booked-slots", {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then((res) => {
            setBookedMap(res.data);
            setFormData({ date: null, timeSlot: "" }); // 2. Reset form
            setAvailableTimeSlots([]);
          });
      })
      .catch((err) => {
        alert("שגיאה בעת שליחת ההזמנה.");
        console.error(err);
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="booking-form">
        <h2>הזמנת הדרכה</h2>

        <label>תאריך:</label>
        <DatePicker
          selected={formData.date}
          onChange={handleDateChange}
          dateFormat="dd/MM/yyyy"
          locale="he"
          minDate={new Date()}
          filterDate={(date) => !isDateDisabled(date)}
          placeholderText="בחר תאריך פנוי"
          className="date-picker"
        />

        <label>משבצת זמן:</label>
        <select
          name="timeSlot"
          value={formData.timeSlot}
          onChange={(e) =>
            setFormData({ ...formData, timeSlot: e.target.value })
          }
          required
        >
          <option value="">בחר משבצת זמן</option>
          {availableTimeSlots.map((slot) => (
            <option key={slot} value={slot}>
              {slot === "morning" ? "בוקר" : "ערב"}
            </option>
          ))}
        </select>

        <button type="submit">שלח</button>
      </div>
    </form>
  );
};

export default BookingForm;
