import React, { useEffect, useState } from "react";
import BookingForm from "../BookingForm"; // Import the form component used for making a booking

// Booking component handles fetching user and appointment IDs, and passes them to BookingForm
const Booking = () => {
  const [userId, setUserId] = useState(null); // State to store the user's ID

  // Retrieve appointmentId directly from localStorage (you could also use URL params or state)
  const appointmentId = localStorage.getItem("appointmentId");

  // useEffect will run when the component mounts or when appointmentId changes
  useEffect(() => {
    // Get the logged-in user's ID from localStorage
    const storedUserId = localStorage.getItem("userId");

    // Save userId to component state
    setUserId(storedUserId);

    // Log userId to the console for debugging
    console.log("🧑‍💼 userId from localStorage:", storedUserId);
  }, [appointmentId]); // Dependency array ensures this effect runs when appointmentId changes

  // Show a loading message until userId is available
  if (!userId) {
    return <div>נטענים פרטי משתמש או פרטי הפגישה...</div>;
  }

  // Once userId is available, render the BookingForm component with userId and appointmentId as props
  return (
    <div className="booking-page">
      <BookingForm userId={userId} appointmentId={appointmentId} />
    </div>
  );
};

export default Booking;
