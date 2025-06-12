import axios from 'axios';

const handleSubmit = async (formData, onSuccess, onError) => {
  try {
    const res = await axios.post('http://localhost:5000/api/bookings', formData);
    alert('Booked successfully!');
    if (onSuccess) onSuccess(res.data);
  } catch (err) {
    alert('Failed to book. Slot might already be taken.');
    if (onError) onError(err);
  }
};

export default handleSubmit;
