import React, { useState } from 'react';
import './BookingForm.css';
import handleSubmit from '../utils/formHandlers';

const BookingForm = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phone: '',
    date: '',
    timeSlot: '',
  });

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handleSubmit(formData);
  };

  return (
    <form className="booking-form" onSubmit={onSubmit}>
      <h2>טופס הזמנת תור</h2>

      <label>שם מלא:</label>
      <input
        type="text"
        name="fullName"
        value={formData.fullName}
        onChange={handleChange}
        required
      />

      <label>אימייל:</label>
      <input
        type="email"
        name="email"
        value={formData.email}
        onChange={handleChange}
        required
      />

      <label>טלפון:</label>
      <input
        type="tel"
        name="phone"
        value={formData.phone}
        onChange={handleChange}
        required
      />

      <label>תאריך:</label>
      <input
        type="date"
        name="date"
        value={formData.date}
        onChange={handleChange}
        required
      />

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

      <button type="submit">שלח</button>
    </form>
  );
};

export default BookingForm;
