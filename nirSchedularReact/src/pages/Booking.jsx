import BookingForm from '../components/BookingForm';
import SlotDisplay from '../components/SlotDisplay';

const Booking = () => (
  <div className="booking-page">
    <h2>Book a Time Slot</h2>
    <BookingForm />
    <SlotDisplay />
  </div>
);

export default Booking;
